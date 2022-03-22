package main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.response.CalendarProjection;
import main.api.response.CalendarResponse;
import main.repository.PostInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @InjectMocks
    CalendarService calendarService;
    @Mock
    PostInfoRepository postInfoRepository;

    private Set<Integer> emptyRequest;
    private Set<Integer> request;
    private Set<Integer> verifyArgsWithEmptyRequest;
    private Set<Integer> verifyArgsWithRequest;
    private List<Integer> yearsFromRepository;
    private CalendarProjection calendarProjection;
    private List<CalendarProjection> calendarProjections;

    @BeforeEach
    void setUp() {
        emptyRequest = new HashSet<>();
        request = new HashSet<>();
        request.add(2034);

        verifyArgsWithEmptyRequest = new HashSet<>();
        verifyArgsWithEmptyRequest.add(Calendar.getInstance().get(Calendar.YEAR));
        verifyArgsWithRequest = new HashSet<>();
        verifyArgsWithRequest.add(2034);

        yearsFromRepository = new ArrayList<>();
        yearsFromRepository.add(1999);
        yearsFromRepository.add(2000);

        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        calendarProjection = factory.createProjection(CalendarProjection.class);
        calendarProjection.setDate(7777);
        calendarProjection.setCount(8888);
        calendarProjections = new ArrayList<>();
        calendarProjections.add(calendarProjection);

    }

    @Test
    void getPostsInCalendarWithEmptyRequest() {

        Mockito.when(postInfoRepository.findYearInCalendar()).thenReturn(yearsFromRepository);
        Mockito.when(postInfoRepository.findDateAndCountPostsByDateInCalendar(Mockito.anySet())).thenReturn(calendarProjections);
        CalendarResponse response = calendarService.getPostsInCalendar(emptyRequest);
        verify(postInfoRepository, times(1)).findDateAndCountPostsByDateInCalendar(verifyArgsWithEmptyRequest);
    }

    @Test
    void getPostsInCalendarWithRequest() {

        Mockito.when(postInfoRepository.findYearInCalendar()).thenReturn(yearsFromRepository);
        Mockito.when(postInfoRepository.findDateAndCountPostsByDateInCalendar(Mockito.anySet())).thenReturn(calendarProjections);
        CalendarResponse response = calendarService.getPostsInCalendar(request);
        assertEquals(response.getPosts().get("7777"), 8888);
        verify(postInfoRepository, times(1)).findDateAndCountPostsByDateInCalendar(verifyArgsWithRequest);
    }
}
