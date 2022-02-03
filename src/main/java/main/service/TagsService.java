package main.service;

import lombok.AllArgsConstructor;
import main.api.response.tagsResponse.TagsResponse;
import main.api.response.tagsResponse.TagsResponseDTO;
import main.repository.TagsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagsService {
    TagsRepository tagsRepository;

    public TagsResponseDTO getTags(String query) {

        List<TagsResponse> tags = tagsRepository.findTagWithWeight();
        return new TagsResponseDTO(query == null ? tags : tags.stream()
                .filter(t -> t.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList()));
    }
}
