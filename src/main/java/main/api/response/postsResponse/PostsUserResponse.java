package main.api.response.postsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostsUserResponse {
    private int id;
    private String name;
}
