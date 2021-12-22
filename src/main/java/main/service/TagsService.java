package main.service;

import main.api.response.tagsResponse.TagsResponse;
import main.api.response.tagsResponse.TagsResponseDTO;
import main.model.Tag;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagsService {

    TagsRepository tagsRepository;

    @Autowired
    public TagsService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;

    }

    public TagsResponseDTO getTags(String query) {

        List<Tag> tagList = new ArrayList<>();
        if (query == null) {
            Iterable<Tag> tags = tagsRepository.getAllTags();
            tags.forEach(a -> tagList.add(a));
        } else {
            Iterable<Tag> tags = tagsRepository.getTagsByName(query);
            tags.forEach(a -> tagList.add(a));
        }

        /** общее кол-во допустимых постов **/
        Optional<Integer> postCount = tagsRepository.postCount();
        int totalPostCount = postCount.get();

        /** map  с количеством тегов **/
        Map<String, Integer> map = new HashMap<>();
        for (Tag t : tagList) {
            map.put(t.getName(), t.getPosts().size());
        }

        /** коэффициент К получаем по формуле **/
        double kCoeff = getСoeffK(map, totalPostCount);

        TagsResponseDTO tagsResponseDTO = new TagsResponseDTO();
        List<TagsResponse> tagsResponse = new ArrayList<>();

        /** заполняем лист тегами  с нормализованным весом тегов **/
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            tagsResponse.add(new TagsResponse(entry.getKey(),
                    ((double) entry.getValue() / totalPostCount) * kCoeff));
        }
        tagsResponseDTO.setTags(tagsResponse);
        return tagsResponseDTO;
    }

    private double getСoeffK(Map<String, Integer> map, int totalPostCount) {
        int maxValueInMap = Collections.max(map.values());  // This will return max value in the Hashmap
        double weight = (double) maxValueInMap / totalPostCount;
        double kCoeff = (double) 1 / weight;
        return kCoeff;
    }
}
