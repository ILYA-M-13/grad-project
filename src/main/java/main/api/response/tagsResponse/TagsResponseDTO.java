package main.api.response.tagsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class TagsResponseDTO {

    private List<TagsResponse>tags;

}
