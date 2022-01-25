package main.service;

import main.api.response.tagsResponse.TagsResponse;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagsService {

    TagsRepository tagsRepository;

    @Autowired
    public TagsService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    public Map<String, List<TagsResponse>> getTags(String query) {

        List<TagsResponse> tags = tagsRepository.findTagWithWeight();
        Map<String, List<TagsResponse>> response = new HashMap<>();

        response.put("tags", query == null ? tags : tags.stream()
                .filter(t -> t.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList()));

        return response;
    }
}
