package main.api.response.postsResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.UserResponse;

@Data
@AllArgsConstructor
public class PostsInfoResponse {

    private int id;
    private long timestamp;
    @JsonIgnoreProperties({"photo"})
    private UserResponse user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
