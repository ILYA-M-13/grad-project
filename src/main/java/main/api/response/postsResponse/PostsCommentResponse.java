package main.api.response.postsResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.commentResponse.CommentResponse;
import main.api.response.UserResponse;

import java.util.List;
@Data
@AllArgsConstructor
public class PostsCommentResponse {
    private int id;
    private long timestamp;
    private boolean active;
    @JsonIgnoreProperties({"photo"})
    private UserResponse user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<CommentResponse> comments;
    private List<String> tags;

}
