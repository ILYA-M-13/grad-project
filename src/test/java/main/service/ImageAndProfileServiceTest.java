package main.service;

import main.api.response.ErrorResponse;
import main.model.User;
import main.repository.UserRepository;
import org.imgscalr.Scalr;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ImageAndProfileServiceTest {

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
    private String anotherEmail = "Test1@test1.ru";
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
                        (multipartFile, true, wrongName, anotherEmail, wrongPassword, principal);
        assertEquals(response.getErrors().get("name"), "Имя указано неверно");
        assertEquals(response.getErrors().get("email"), "Этот e-mail уже зарегистрирован");
        assertEquals(response.getErrors().get("password"), "Пароль короче 6-ти символов");
        assertEquals(response.isResult(), false);

    }

    @Test
    void editMyProfileWithPhotoWithValidResponse() throws IOException {

        Mockito.when(postInfoService.getAuthUser(Mockito.any())).thenReturn(user);
        MockedStatic<Scalr> classMock = mockStatic(Scalr.class);
        classMock.when(() -> Scalr.resize(Mockito.any(),
                Mockito.any(Scalr.Method.class),
                Mockito.anyInt(), Mockito.anyInt())).thenReturn(new BufferedImage(1, 1, 1));

        ErrorResponse response =
                imageAndProfileService.editMyProfileWithPhoto
                        (multipartFile, true, name, anotherEmail, password, principal);
        assertEquals(response.isResult(), true);
        classMock.close();
    }
}
