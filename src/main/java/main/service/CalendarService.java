package main.service;

import lombok.AllArgsConstructor;
import main.api.response.CalendarResponse;
import main.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CalendarService {
    CalendarRepository calendarRepository;

    public CalendarResponse getPostsInCalendar(String year) {
        List<Integer> years = calendarRepository.findPostsInCalendar();
        Map<String,Long> posts = calendarRepository.findCountPostsByDateInCalendar(year);
        return new CalendarResponse(years,posts);
    }
}
