package main.service;

import main.api.response.tagsResponse.TagsResponse;
import main.api.response.tagsResponse.TagsResponseDTO;
import main.model.Post;
import main.model.Tag;
import main.repository.PostInfoRepository;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class TagsService {

    TagsRepository tagsRepository;
    PostInfoRepository postInfoRepository;

    @Autowired
    public TagsService(TagsRepository tagsRepository, PostInfoRepository postInfoRepository) {
        this.tagsRepository = tagsRepository;
        this.postInfoRepository = postInfoRepository;

    }

    public TagsResponseDTO getTags(String query) {

        List<Tag> tagList = new ArrayList<>();
        if (query == null) {
            Iterable<Tag> tags = tagsRepository.findAll();
            tags.forEach(a -> tagList.add(a));
        } else {
            Iterable<Tag> tags = tagsRepository.findTagsByName(query);
            tags.forEach(a -> tagList.add(a));
        }

        /** общее кол-во допустимых постов **/
        Collection<Post> postCount = postInfoRepository.postCount();
        int totalPostCount = postCount.size();

        /** map  с количеством тегов **/
        Map<String, Long> map = new HashMap<>();
        for (Tag t : tagList) {
            map.put(t.getName(),  t.getPosts().stream()
                                        .filter(p -> p.isActive())
                                        .filter(p->p.getModerationStatus().getName() == "ACCEPTED")
                                        .filter(p->p.getTime().before(new Date()))
                                        .count());
        }

        /** коэффициент К получаем по формуле **/
        double kCoeff = getCoeffK(map, totalPostCount);
        TagsResponseDTO tagsResponseDTO = new TagsResponseDTO();
        List<TagsResponse> tagsResponse = new ArrayList<>();

        /** заполняем лист тегами  с нормализованным весом тегов **/
        for (Map.Entry<String, Long> entry : map.entrySet()) {

            double normWeight = ((double) entry.getValue() / totalPostCount) * kCoeff;
            tagsResponse.add(new TagsResponse(entry.getKey(),roundNumber(normWeight)));
        }
        tagsResponseDTO.setTags(tagsResponse);
        return tagsResponseDTO;
    }

    private double getCoeffK(Map<String, Long> map, int totalPostCount) {
        long maxValueInMap = Collections.max(map.values());  // This will return max value in the Hashmap
        double weight = (double) maxValueInMap / totalPostCount;
        double kCoeff = (double) 1 / weight;
        return kCoeff;
    }

    private double roundNumber(double number){
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(2,RoundingMode.DOWN).doubleValue();
    }
}
