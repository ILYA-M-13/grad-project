package main.api.response.commentResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.UserResponse;

@Data
@AllArgsConstructor
public class CommentResponse {
    private int id;
    private long timestamp;
    private String text;
    private UserResponse user;
}
