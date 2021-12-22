package main.api.response.tagsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagsResponse {
    private String name;
    private double weight;
}


