package main.api.response.tagsResponse;

import lombok.Data;

import java.util.List;

@Data
public class TagsResponseDTO {
    private List<TagsResponse> tags;
}
