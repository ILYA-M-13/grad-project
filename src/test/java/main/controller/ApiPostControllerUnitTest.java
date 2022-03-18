package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.response.postsResponse.PostsResponseDTO;
import main.repository.PostInfoRepository;
import main.repository.TagsRepository;
import main.repository.UserRepository;
import main.service.PostInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ApiPostControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PostInfoRepository postInfoRepository;
    @MockBean
    private TagsRepository tagsRepository;
    @Mock
    private PostInfoService postInfoService;
    @MockBean
    UserDetailsService userDetailsService;
    @InjectMocks
    ApiPostController apiPostController;

    private PasswordEncoder BCRYPT = new BCryptPasswordEncoder(12);
    private int offset = 0;
    private int limit = 10;
    PostsResponseDTO postsResponseDTO = new PostsResponseDTO(0, new ArrayList<>());

    // @Test
//    void searchByTeg() throws Exception {
//        Mockito.when(postInfoService.getPostsByTag(Mockito.anyInt(),Mockito.anyInt(),Mockito.any())).thenReturn(postsResponseDTO);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/post/byTag").param("offset","0")
//                .param("limit","10").param("tag","Spring")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));
//    }

    @Test
    void searchByTeg() throws Exception {
        Mockito.when(postInfoService.getPostsByTag(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(postsResponseDTO);
        ResponseEntity<PostsResponseDTO> responseEntity = apiPostController.searchByTag(offset, limit, "Spring");

        verify(postInfoService, times(1)).getPostsByTag(offset, limit, "Spring");
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertEquals(responseEntity.getBody().getCount(), 0);
    }
}