package main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.EditProfileRequest;
import main.api.request.PassChangeRequest;
import main.api.request.PassRestoreRequest;
import main.api.request.RegistrationRequest;
import main.api.response.ErrorResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthCheckServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @InjectMocks
    AuthCheckService authCheckService;
    @Mock
    PostInfoService postInfoService;
    @Mock
    private Principal principal;
    @Mock
    UserRepository userRepository;
    @Mock
    CaptchaRepository captchaRepository;
    @Mock
    MailService mailService;


    private User user;
    private EditProfileRequest incorrectRequest;
    private EditProfileRequest validRequest;
    private RegistrationRequest incorrectRegistrationRequest;
    private RegistrationRequest validRegistrationRequest;
    private List<User>users;
    private CaptchaCode captchaCode;
    private PassChangeRequest incorrectPassChangeRequest;
    private PassChangeRequest validPassChangeRequest;
    private PassRestoreRequest passRestoreRequest;


    @BeforeEach
    void setUp() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    user = new User();
    user.setEmail("Test@mail.ru");
    users = new ArrayList<>();
    users.add(user);

    incorrectRequest = new EditProfileRequest();
    incorrectRequest.setEmail("Petrov@mail.ru");
    incorrectRequest.setName("oijh8776g");
    incorrectRequest.setRemovePhoto(true);
    incorrectRequest.setPassword("123");

    validRequest = new EditProfileRequest();
        validRequest.setEmail("Petrov@mail.ru");
        validRequest.setName("Petrov Petr");
        validRequest.setRemovePhoto(true);
        validRequest.setPassword("123456");

        incorrectRegistrationRequest = new RegistrationRequest();
        incorrectRegistrationRequest.setEmail("Test@mail.ru");
        incorrectRegistrationRequest.setName("oh67ttyu");
        incorrectRegistrationRequest.setPassword("123");
        incorrectRegistrationRequest.setCaptcha("1");
        incorrectRegistrationRequest.setCaptchaSecret("2");

        validRegistrationRequest = new RegistrationRequest();
        validRegistrationRequest.setEmail("Test1@mail.ru");
        validRegistrationRequest.setName("Petrov Petr");
        validRegistrationRequest.setPassword("123456");
        validRegistrationRequest.setCaptcha("1");
        validRegistrationRequest.setCaptchaSecret("2");

        captchaCode = new CaptchaCode();
        captchaCode.setCode("1");
        captchaCode.setSecretCode("2");

        incorrectPassChangeRequest = new PassChangeRequest();
        incorrectPassChangeRequest.setCode("5");
        incorrectPassChangeRequest.setPassword("12");
        incorrectPassChangeRequest.setCaptchaSecret("3");
        incorrectPassChangeRequest.setCaptcha("000");
        validPassChangeRequest = new PassChangeRequest();
        validPassChangeRequest.setCode("000");
        validPassChangeRequest.setPassword("123456");
        validPassChangeRequest.setCaptchaSecret("2");
        validPassChangeRequest.setCaptcha("1");

        passRestoreRequest = new PassRestoreRequest();
        passRestoreRequest.setEmail("Test@test.ru");

    }

    @Test
    void editMyProfileWithIncorrectRequest() {

        Mockito.when(postInfoService.getAuthUser(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        ErrorResponse response = authCheckService.editMyProfile(incorrectRequest,principal);
        assertEquals(response.isResult(),false);
        assertEquals(response.getErrors().get("name"),"Имя указано неверно");
        assertEquals(response.getErrors().get("email"),"Этот e-mail уже зарегистрирован");
        assertEquals(response.getErrors().get("password"),"Пароль короче 6-ти символов");
    }

    @Test
    void editMyProfileWithValidRequest() {

        Mockito.when(postInfoService.getAuthUser(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        ErrorResponse response = authCheckService.editMyProfile(validRequest,principal);
        assertEquals(response.isResult(),true);

    }

    @Test
    void getRegistrationWithIncorrectRequest() {

        Mockito.when(captchaRepository.findCaptchaCodeBySecretCode(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findAll()).thenReturn(users);
        ErrorResponse response = authCheckService.getRegistration(incorrectRegistrationRequest);
        assertEquals(response.getErrors().get("email"),"Этот e-mail уже зарегистрирован");
        assertEquals(response.getErrors().get("name"),"Имя указано неверно");
        assertEquals(response.getErrors().get("password"),"Пароль короче 6-ти символов");
        assertEquals(response.getErrors().get("captcha"),"Код с картинки введён неверно или такого кода нет");
        assertEquals(response.isResult(),false);
    }

    @Test
    void getRegistrationWithValidRequest() {

        Mockito.when(captchaRepository.findCaptchaCodeBySecretCode(Mockito.any())).thenReturn(Optional.of(captchaCode));
        Mockito.when(userRepository.findAll()).thenReturn(users);
        ErrorResponse response = authCheckService.getRegistration(validRegistrationRequest);
        assertEquals(response.isResult(),true);
    }

    @Test
    void getRestorePasswordWithUser(){
       Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
       ErrorResponse response = authCheckService.getRestorePassword(passRestoreRequest);
        assertEquals(response.isResult(),true);
    }

    @Test
    void getRestorePasswordWithoutUser(){

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        ErrorResponse response = authCheckService.getRestorePassword(new PassRestoreRequest());
        assertEquals(response.isResult(),false);
    }

    @Test
    void getChangePasswordWithIncorrectRequest() {

        Mockito.when(userRepository.findByCode(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(captchaRepository.findCaptchaCodeBySecretCode(Mockito.any())).thenReturn(Optional.of(captchaCode));
        ErrorResponse response = authCheckService.getChangePassword(incorrectPassChangeRequest);
        assertEquals(response.getErrors().get("code"),"Ссылка для восстановления пароля устарела.<a href=\"/auth/restore\">Запросить ссылку снова</a>");
        assertEquals(response.getErrors().get("captcha"),"Код с картинки введён неверно");
        assertEquals(response.getErrors().get("password"),"Пароль короче 6-ти символов");
        assertEquals(response.isResult(),false);

    }

    @Test
    void getChangePasswordWithValidRequest() {

        Mockito.when(userRepository.findByCode(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(captchaRepository.findCaptchaCodeBySecretCode(Mockito.any())).thenReturn(Optional.of(captchaCode));
        ErrorResponse response = authCheckService.getChangePassword(validPassChangeRequest);
        assertEquals(response.isResult(),true);

    }
}
