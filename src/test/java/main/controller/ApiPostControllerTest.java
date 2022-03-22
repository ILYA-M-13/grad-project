package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.PostRequest;
import main.api.response.ErrorResponse;
import main.api.response.UserResponse;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.model.Post;
import main.repository.PostInfoRepository;
import main.service.PostInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApiPostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @Mock
    private PostInfoService postInfoService;
    @Mock
    private PostInfoRepository postInfoRepository;
    @MockBean
    UserDetailsService userDetailsService;
    @InjectMocks
    ApiPostController apiPostController;
    @Mock
    private Principal principal;

    private int offset = 0;
    private int limit = 10;
    private ErrorResponse errorResponse = new ErrorResponse();
    private Post post = new Post();
    private PostsResponseDTO postsResponseDTO;
    private PostsCommentResponse postsCommentResponse;
    private PostRequest postRequest = new PostRequest();

    @BeforeEach
    void setUp() {

        postsCommentResponse = new PostsCommentResponse
                (1, 100000000, true, new UserResponse(1, "Test", "Test"), "Test",
                        "Test", 1, 1, 2, new ArrayList<>(), new ArrayList<>());

        postsResponseDTO = new PostsResponseDTO(0, new ArrayList<>());

    }

    @Test
    void searchByTegUnit() throws Exception {

        Mockito.when(postInfoService.getPostsByTag(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(postsResponseDTO);
        ResponseEntity<PostsResponseDTO> responseEntity = apiPostController.searchByTag(offset, limit, "Spring");
        verify(postInfoService, times(1)).getPostsByTag(offset, limit, "Spring");
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertEquals(responseEntity.getBody().getCount(), 0);
    }

    @Test
    void searchByIdWithActivePost() throws Exception {

        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(postInfoService.getPostsById(Mockito.anyInt())).thenReturn(postsCommentResponse);
        ResponseEntity<PostsCommentResponse> responseEntity = apiPostController.searchById(1, null);
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertEquals(responseEntity.getBody().getTitle(), "Test");
    }

    @Test
    void searchByIdWithoutActivePost() throws Exception {

        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.empty());
        ResponseEntity<PostsCommentResponse> responseEntity = apiPostController.searchById(1, null);
        assertEquals(responseEntity.getStatusCode().value(), 400);
    }

    @Test
    void searchByIdWithPrincipal() throws Exception {

        Mockito.when(postInfoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(postInfoService.getPostsById(Mockito.anyInt(), Mockito.any())).thenReturn(postsCommentResponse);
        ResponseEntity<PostsCommentResponse> responseEntity = apiPostController.searchById(1, principal);
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertEquals(responseEntity.getBody().getViewCount(), postsCommentResponse.getViewCount());
    }

    @Test
    void refactorPost() {

        errorResponse.setResult(true);
        Mockito.when(postInfoService.checkPost(Mockito.anyInt(), Mockito.any())).thenReturn(true);
        Mockito.when(postInfoService.getRefactorPost(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(errorResponse);
        ResponseEntity<?> responseEntity = apiPostController.refactorPost(postRequest, principal, 1);
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertEquals(responseEntity.getBody().toString(), errorResponse.toString());
    }
}