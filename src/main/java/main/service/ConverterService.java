package main.service;

import lombok.AllArgsConstructor;
import main.api.response.CommentResponse;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.UserResponse;
import main.model.*;
import main.repository.PostInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConverterService {
    static final int MAX_CHAR_IN_POST = 150;
    static final int MILLISEC_IN_SEC = 1000;
    PostInfoRepository postInfoRepository;

    public PostsInfoResponse convertPostToDTO(Post post) {
        return new PostsInfoResponse(post.getId(),
                post.getTime().getTime() / MILLISEC_IN_SEC,
                convertUserToDto(post.getUser()),
                post.getTitle(),
                formatText(post.getText()),
                likeCount(post.getPostVotes()),
                dislikeCount(post.getPostVotes()),
                post.getPostComments().size(),
                post.getViewCount());
    }

    public UserResponse convertUserToDto(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getPhoto());

    }

    public PostsCommentResponse convertPostToPostCommentResponse(Post post) {
        List<Tag> tags = post.getTags();
        List<String> strings = tags.stream().map(Tag::getName).collect(Collectors.toList());
        List<PostComment> postComments = post.getPostComments();
        List<CommentResponse> commentResponses = postComments.stream()
                .map(this::convertCommentToCommentResponse)
                .collect(Collectors.toList());
        return new PostsCommentResponse(post.getId(),
                post.getTime().getTime() / MILLISEC_IN_SEC,
                post.isActive(),
                convertUserToDto(post.getUser()),
                post.getTitle(),
                post.getText(),
                likeCount(post.getPostVotes()),
                dislikeCount(post.getPostVotes()),
                post.getViewCount(),
                commentResponses,
                strings);
    }

    public CommentResponse convertCommentToCommentResponse(PostComment postComment) {
        return new CommentResponse(postComment.getId(),
                postComment.getTime().getTime() / MILLISEC_IN_SEC,
                postComment.getText(),
                convertUserToDto(postComment.getUser()));
    }

    private String formatText(String text) {
        text = text.replaceAll("\\<.*?>", "");
        return text.length() > MAX_CHAR_IN_POST ? text.substring(0, MAX_CHAR_IN_POST) + "..." : text;
    }
    private int likeCount(List<PostVote>list){
        return (int) list.stream().filter(pv -> pv.getValue()==1).count();
    }
    private int dislikeCount(List<PostVote>list){
        return (int) list.stream().filter(pv -> pv.getValue()==-1).count();
    }
}