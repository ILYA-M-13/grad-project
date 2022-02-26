package main.service;

import lombok.AllArgsConstructor;
import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.ErrorResponse;
import main.api.response.StatisticsResponse;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.model.*;
import main.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class PostInfoService {

    private final PostInfoRepository postInfoRepository;
    private final UserRepository userRepository;
    private final ConverterService converter;
    private final TagsRepository tagsRepository;
    private final SettingsRepository settingsRepository;
    private final PostVoteRepository postVoteRepository;

    public PostsResponseDTO getModerationPosts(int offset, int limit, String status, Principal principal) {
        User moderator = getAuthUser(principal);
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findNewPostByModerator(pageable);
        switch (status) {
            case "accepted":
                pagePosts = postInfoRepository.findAcceptedPostByModerator(pageable, moderator.getId());
                break;
            case "declined":
                pagePosts = postInfoRepository.findDeclinedPostByModerator(pageable, moderator.getId());
                break;
        }
        return getPostsResponseDTO(pagePosts);
    }

    public ErrorResponse getModeration(ModerationRequest moderationRequest, Principal principal) {
        ErrorResponse resultResponse = new ErrorResponse();
        User moderator = getAuthUser(principal);
        int id = moderationRequest.getPostId();
        if (moderator.isModerator() && postInfoRepository.findById(id).isPresent()) {
            Post post = postInfoRepository.findById(id).get();
            String status = moderationRequest.getDecision();

            post.setModerationStatus(status.equals("accept") ? ModerationStatus.ACCEPTED : ModerationStatus.DECLINED);
            post.setModeratorUser(moderator);
            postInfoRepository.save(post);
            resultResponse.setResult(true);

        } else {
            resultResponse.setResult(false);
        }
        return resultResponse;
    }

    public boolean checkPermission(Principal principal) {
        if (!settingsRepository.findGlobalSettingByName("STATISTICS_IS_PUBLIC").equals("YES")) {
            if (principal != null) {
                User user = getAuthUser(principal);
                return user.isModerator();
            }
            return false;
        }
        return true;
    }

    public StatisticsResponse getStatisticsMy(Principal principal) {
        User user = getAuthUser(principal);
        return postInfoRepository.getMyStatistics(user.getId());
    }

    public StatisticsResponse getStatisticsAll() {
        return postInfoRepository.getAllStatistics();
    }

    public ErrorResponse getVote(int postId, Principal principal, int value) {
        ErrorResponse errorResponse = new ErrorResponse();
        Optional<Post> optionalPost = postInfoRepository.findActivePostById(postId);
        if (optionalPost.isEmpty()) {
            return errorResponse;
        }
        User user = getAuthUser(principal);
        Optional<PostVote> postVoteOptional = postVoteRepository.getPostVote(user.getId(), postId);
        if (postVoteOptional.isPresent()) {
            PostVote postVote = postVoteOptional.get();
            if (postVote.getValue() != value) {
                postVote = converter.getPostVote(postVote.getId(),
                        postVote.getPost(),
                        postVote.getUser(),
                        value);
                postVoteRepository.save(postVote);
                errorResponse.setResult(true);
            }
            return errorResponse;
        }
        PostVote postVote = converter.getPostVote(null, optionalPost.get(), user, value);
        postVoteRepository.save(postVote);
        errorResponse.setResult(true);
        return errorResponse;
    }

    public PostsResponseDTO getAllPosts(int offset, int limit, ModePosts modePosts) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllOrderByTimeDesc(pageable);
        switch (modePosts) {
            case early:
                pagePosts = postInfoRepository.findALLOrderByTimeAsc(pageable);
                break;
            case popular:
                pagePosts = postInfoRepository.findALLOrderByPostCommentsDesc(pageable);
                break;
            case best:
                pagePosts = postInfoRepository.findALLOrderByPostVotes(pageable);
                break;
        }
        return getPostsResponseDTO(pagePosts);
    }

    public PostsResponseDTO getMyPosts(int offset, int limit, MyPostStatus status, Principal principal) {
        String email = principal.getName();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findMyInactivePost(pageable, email);
        switch (status) {
            case pending:
                pagePosts = postInfoRepository.findMyPendingPost(pageable, email);
                break;
            case declined:
                pagePosts = postInfoRepository.findMyDeclinedPost(pageable, email);
                break;
            case published:
                pagePosts = postInfoRepository.findMyPublishedPost(pageable, email);
                break;
        }
        return getPostsResponseDTO(pagePosts);
    }

    public PostsResponseDTO getPostsByQuery(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);

        Page<Post> pagePosts = query.matches("")
                ? postInfoRepository.findAllOrderByTimeDesc(pageable)
                : postInfoRepository.findAllByQuery(pageable, query);

        return getPostsResponseDTO(pagePosts);
    }

    public PostsResponseDTO getPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllByDate(pageable, date);
        return getPostsResponseDTO(pagePosts);
    }

    public PostsResponseDTO getPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllByTag(pageable, tag);
        return getPostsResponseDTO(pagePosts);
    }

    public PostsCommentResponse getPostsById(int id) {
        Post post = postInfoRepository.findActivePostById(id).get();
        post.setViewCount(post.getViewCount() + 1);
        postInfoRepository.save(post);
        return converter.convertPostToPostCommentResponse(post);
    }

    public PostsCommentResponse getPostsById(int id, Principal principal) {
        Optional<User> user = userRepository.findByEmail(principal.getName());
        Post post = postInfoRepository.findById(id).get();
        String email = post.getUser().getEmail();
        if (user.get().isModerator() || principal.getName().equals(email)) {
            return converter.convertPostToPostCommentResponse(post);
        } else {
            Post activePost = postInfoRepository.findActivePostById(id).get();
            String activeEmail = activePost.getUser().getEmail();
            activePost.setViewCount(post.getViewCount() + 1);
            postInfoRepository.save(activePost);
            return converter.convertPostToPostCommentResponse(activePost);
        }
    }

    public ErrorResponse getNewPost(PostRequest postRequest, Principal principal) {
        User user = getAuthUser(principal);
        Post post = new Post();
        Map<String, String> error = new HashMap<>();

        if (postRequest.getTitle().length() < 3) {
            error.put("title", "Заголовок не установлен");
        } else post.setTitle(postRequest.getTitle());

        if (converter.formatText(postRequest.getText()).length() < 50) {
            error.put("text", "Текст публикации слишком короткий");
        } else post.setText(converter.formatText(postRequest.getText()));

        post.setTime(postRequest.getTimestamp() <= new Date().getTime() ?
                new Date() : new Date(postRequest.getTimestamp()));
        post.setModerationStatus(settingsRepository.findGlobalSettingByName("POST_PREMODERATION").equals("YES") ?
                ModerationStatus.NEW : ModerationStatus.ACCEPTED);
        post.setActive(postRequest.getActive() == 1);
        post.setUser(user);
        post.setViewCount(0);
        post.setTags(getTagList(postRequest.getTags()));
        return getErrorResponse(error, post);
    }

    public ErrorResponse getRefactorPost(PostRequest postRequest, Principal principal, int id) {
        User user = getAuthUser(principal);
        Post post = postInfoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("post not found"));
        String email = post.getUser().getEmail();
        Map<String, String> error = new HashMap<>();

        if (user.isModerator() || user.getEmail().equals(email)) {
            if (postRequest.getTitle().length() < 3) {
                error.put("title", "Заголовок не установлен");
            } else post.setTitle(postRequest.getTitle());

            if (converter.formatText(postRequest.getText()).length() < 50) {
                error.put("text", "Текст публикации слишком короткий");
            } else post.setText(converter.formatText(postRequest.getText()));

            post.setTime(postRequest.getTimestamp() <= new Date().getTime() ?
                    new Date() : new Date(postRequest.getTimestamp()));
            post.setModerationStatus(user.isModerator() ? post.getModerationStatus() : ModerationStatus.NEW);
            post.setActive(postRequest.getActive() == 1);
            post.setViewCount(post.getViewCount());
            post.setTags(getTagList(postRequest.getTags()));
        }
        return getErrorResponse(error, post);
    }

    public User getAuthUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }

    private Set<Tag> getTagList(Set<String> strings) {
        Set<Tag> tags = new HashSet<>();
        strings.stream().forEach(s -> {
            Optional<Tag> tagOptional = tagsRepository.findTagByName(s);
            if (tagOptional.isPresent()) {
                tags.add(tagOptional.get());
            } else {
                Tag tag = new Tag();
                tag.setName(s);
                tagsRepository.save(tag);
                tags.add(tag);
            }
        });
        return tags;
    }

    private ErrorResponse getErrorResponse(Map<String, String> error, Post post) {
        ErrorResponse errorResponse = new ErrorResponse();
        if (error.isEmpty()) {
            postInfoRepository.save(post);
            errorResponse.setResult(true);
        } else {
            errorResponse.setResult(false);
            errorResponse.setErrors(error);
        }
        return errorResponse;
    }

    private PostsResponseDTO getPostsResponseDTO(Page<Post> pagePosts) {
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        List<PostsInfoResponse> list = new ArrayList<>();
        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

}
