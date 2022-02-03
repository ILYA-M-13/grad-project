package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private int id;
    private long timestamp;
    private String text;
    private UserResponse user;
}
