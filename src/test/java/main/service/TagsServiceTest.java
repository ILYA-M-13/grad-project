package main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.response.tagsResponse.TagsResponse;
import main.api.response.tagsResponse.TagsResponseDTO;
import main.repository.TagsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TagsServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @InjectMocks
    TagsService tagsService;
    @Mock
    TagsRepository tagsRepository;

    private TagsResponse tagsResponse;
    private List<TagsResponse>tags = new ArrayList<>();
    private String nullString;
    private String query = "Test";
    private String queryUpper = "TEST";

    @BeforeEach
    void setUp() {
        tagsResponse = new TagsResponse("Test",0.1);
        tags.add(tagsResponse);
        tags.add(new TagsResponse("Teg",0.2));
    }

        @Test
    void getTagsWithoutQuery() {

        Mockito.when(tagsRepository.findTagWithWeight()).thenReturn(tags);
            TagsResponseDTO response = tagsService.getTags(nullString);
            assertEquals(response.getTags().size(),2);
    }

    @Test
    void getTagsWithQuery() {

        Mockito.when(tagsRepository.findTagWithWeight()).thenReturn(tags);
        TagsResponseDTO response = tagsService.getTags(query);
        assertEquals(response.getTags().size(),1);
    }

    @Test
    void getTagsWithUpperCaseQuery() {

        Mockito.when(tagsRepository.findTagWithWeight()).thenReturn(tags);
        TagsResponseDTO response = tagsService.getTags(queryUpper);
        assertEquals(response.getTags().size(),1);
    }
}
