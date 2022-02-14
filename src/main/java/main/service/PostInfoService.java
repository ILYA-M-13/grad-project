package main.service;

import lombok.AllArgsConstructor;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.model.ModePosts;
import main.model.MyPostStatus;
import main.model.Post;
import main.model.User;
import main.repository.PostInfoRepository;
import main.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostInfoService {
    private final PostInfoRepository postInfoRepository;
    private final UserRepository userRepository;
    private final ConverterService converter;
    private final AuthCheckService authCheckService;

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
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
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
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

    public PostsResponseDTO getPostsByQuery(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);

        Page<Post> pagePosts = query.matches("")
                ? postInfoRepository.findAllOrderByTimeDesc(pageable)
                : postInfoRepository.findAllByQuery(pageable, query);

        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);

        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }

        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

    public PostsResponseDTO getPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllByDate(pageable, date);
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

    public PostsResponseDTO getPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllByTag(pageable, tag);
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
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

}
