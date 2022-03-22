package main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.response.ErrorResponse;
import main.model.User;
import main.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ImageAndProfileServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @InjectMocks
    ImageAndProfileService imageAndProfileService;
    @Mock
    PostInfoService postInfoService;
    @Mock
    UserRepository userRepository;
    @Mock
    Principal principal;

    private User user;
    private String wrongName = "987hm";
    private String name = "Petr Petrov";
    private String anotherEmail= "Test1@test1.ru";
    private String wrongPassword = "123";
    private String password = "123456";
    private ClassPathResource classPathResource1 = new ClassPathResource("filename.jpg");
    private MockMultipartFile multipartFile;

    public ImageAndProfileServiceTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("Test@test.ru");
        user.setName("Test");
        user.setPassword("123456");
        multipartFile = new MockMultipartFile("data", "filename.jpg", "text/plain", "application/octet-stream".getBytes());
    }

    @Test
    void editMyProfileWithPhotoWithErrorResponse() throws IOException {

        Mockito.when(postInfoService.getAuthUser(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(new User()));
        ErrorResponse response =
                imageAndProfileService.editMyProfileWithPhoto
                        (multipartFile,true,wrongName,anotherEmail,wrongPassword,principal);
        assertEquals(response.getErrors().get("name"),"Имя указано неверно");
        assertEquals(response.getErrors().get("email"),"Этот e-mail уже зарегистрирован");
        assertEquals(response.getErrors().get("password"),"Пароль короче 6-ти символов");
        assertEquals(response.isResult(),false);

    }

    @Test
    void editMyProfileWithPhotoWithValidResponse() throws IOException {

        Mockito.when(postInfoService.getAuthUser(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
                ErrorResponse response =
                imageAndProfileService.editMyProfileWithPhoto
                        (multipartFile,true,name,anotherEmail,password,principal);
        assertEquals(response.isResult(),true);

    }
}
