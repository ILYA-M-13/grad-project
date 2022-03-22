package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.CommentRequest;
import main.api.response.ErrorResponse;
import main.api.response.commentResponse.IdCommentResponse;
import main.service.CommentService;
import main.service.ImageAndProfileService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.*;


@ExtendWith(MockitoExtension.class)
public class ApiGeneralControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @InjectMocks
    ApiGeneralController apiGeneralController;
    @Mock
    private Principal principal;
    @Mock
    ImageAndProfileService imageAndProfileService;
    @Mock
    CommentService commentService;
    @Mock
    PostInfoService postInfoService;

    private MultipartFile file;
private CommentRequest commentRequest;
private ErrorResponse errorResponse;

@BeforeEach
void setUp() {
    commentRequest = new CommentRequest();
    commentRequest.setText("");
    commentRequest.setParentId(0);


}

@Test
    void image() throws IOException {
        Mockito.when(imageAndProfileService.checkImage(Mockito.any())).thenReturn(false);
        Mockito.when(imageAndProfileService.errorLoad()).thenReturn(new ErrorResponse());
        ResponseEntity<?> response = apiGeneralController.image(file);
        assertEquals(response.getStatusCode(),BAD_REQUEST);

    }

    @Test
    void commentWithValidText() {

        Mockito.when(commentService.checkRequest(Mockito.any())).thenReturn(true);
        Mockito.when(commentService.addComment(Mockito.any(),Mockito.any())).thenReturn(new IdCommentResponse(1));
        ResponseEntity<?> response = apiGeneralController.comment(commentRequest,principal);
        assertEquals(response.getStatusCode(),OK);

    }

    @Test
    void commentWithIncorrectText() {

        Mockito.when(commentService.checkRequest(Mockito.any())).thenReturn(false);
        Mockito.when(commentService.errorResponse(Mockito.any())).thenReturn(errorResponse);
        ResponseEntity<?> response = apiGeneralController.comment(commentRequest,principal);
        assertEquals(response.getStatusCode(),BAD_REQUEST);
    }

    @Test
    void statisticsAll() {
        Mockito.when(postInfoService.checkPermission(Mockito.any())).thenReturn(false);
        ResponseEntity response = apiGeneralController.statisticsAll(principal);
        assertEquals(response.getStatusCode(),UNAUTHORIZED);
    }
}
