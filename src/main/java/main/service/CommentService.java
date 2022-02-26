package main.service;

import lombok.AllArgsConstructor;
import main.api.request.CommentRequest;
import main.api.response.ErrorResponse;
import main.api.response.commentResponse.IdCommentResponse;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import main.repository.CommentRepository;
import main.repository.PostInfoRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostInfoRepository postInfoRepository;
    private final PostInfoService postInfoService;
    private final CommentRepository commentRepository;
    private final int MIN_LENGTH_COMMENT = 5;

    public Map<String, String> checkRequest(CommentRequest commentRequest) {
        Map<String, String> errors = new HashMap<>();
        if (commentRequest.getText().length() < MIN_LENGTH_COMMENT) {
            errors.put("text", "Текст комментария не задан или слишком короткий");
        }

        Optional<Post> post = postInfoRepository.findActivePostById(commentRequest.getPostId());
        if (post.isEmpty()) {
            errors.put("post", "Post not found");
        }

        if (commentRequest.getParentId() != null) {
            Optional<PostComment> postComment = commentRepository.findById(commentRequest.getParentId());
            if (postComment.isEmpty()) {
                errors.put("comment", "Comment not found");
            }
        }
        return errors;
    }

    public ErrorResponse errorResponse(Map<String, String> error) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResult(false);
        errorResponse.setErrors(error);
        return errorResponse;
    }

    public IdCommentResponse addComment(CommentRequest commentRequest, Principal principal) {
        User user = postInfoService.getAuthUser(principal);
        Optional<Post> post = postInfoRepository.findActivePostById(commentRequest.getPostId());
        PostComment parentComment = null;
        if (commentRequest.getParentId() != null) {
            Optional<PostComment> optionalComment = commentRepository.findById(commentRequest.getParentId());
            parentComment = optionalComment.isPresent() ? optionalComment.get() : null;
        }
        PostComment postComment = new PostComment();
        postComment.setUser(user);
        postComment.setText(commentRequest.getText().replaceAll("\\<.*?>", ""));
        postComment.setTime(Date.from(Instant.now()));
        postComment.setPost(post.get());
        postComment.setParentComment(parentComment);
        commentRepository.save(postComment);
        return new IdCommentResponse(postComment.getId());
    }
}