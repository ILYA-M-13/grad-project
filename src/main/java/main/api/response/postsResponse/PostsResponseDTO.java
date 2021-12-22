package main.api.response.postsResponse;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostsResponseDTO {
    private int count;
    private List<PostsInfoResponse> posts;
}
