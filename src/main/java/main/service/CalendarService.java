package main.service;

import lombok.AllArgsConstructor;
import main.api.response.CalendarProjection;
import main.api.response.CalendarResponse;
import main.repository.PostInfoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CalendarService {
    PostInfoRepository postInfoRepository;

    public CalendarResponse getPostsInCalendar(Set<Integer> yearRequest) {
        Set<Integer> year = new HashSet<>();
        if (yearRequest == null || yearRequest.isEmpty()) {
            year.add(Calendar.getInstance().get(Calendar.YEAR));
        } else {
            year.addAll(yearRequest);
        }
        List<Integer> years = postInfoRepository.findYearInCalendar();
        List<CalendarProjection> postsProj = postInfoRepository.findDateAndCountPostsByDateInCalendar(year);
        Map<String, Long> posts = new HashMap<>();
        for (CalendarProjection projection : postsProj) {
            posts.put(projection.getDate(), projection.getCount());
        }
        return new CalendarResponse(years, posts);
    }


}
