package main.service;

import lombok.AllArgsConstructor;
import main.api.request.RegistrationRequest;
import main.api.response.RegistrationErrorResponse;
import main.api.response.authCheckResponse.AuthCheckResponseDTO;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthCheckService {
    UserRepository userRepository;
    CaptchaRepository captchaRepository;

    public AuthCheckResponseDTO getAuthCheckInfo() {
        AuthCheckResponseDTO authCheckResponse = new AuthCheckResponseDTO();

        return authCheckResponse;
    }

    public RegistrationErrorResponse getRegister(RegistrationRequest registrationRequest) {
        Optional<CaptchaCode> captchaCode =
                captchaRepository.findCaptchaCodeBySecretCode(registrationRequest.getCaptchaSecret());
        Iterable<User> users = userRepository.findAll();
        Map<String, String> errorResponse = new HashMap<>();

        users.forEach(u -> {
            if (u.getEmail().equalsIgnoreCase(registrationRequest.getEmail())) {
                errorResponse.put("email", "Этот e-mail уже зарегистрирован");
            }
        });
        if (!registrationRequest.getName().matches("[А-Яа-яA-Za-z-]+([А-Яа-яA-Za-z-\\s]+)?")) {
            errorResponse.put("name", "Имя указано неверно");
        }
        if (registrationRequest.getPassword().length() < 6) {
            errorResponse.put("password", "Пароль короче 6-ти символов");
        }
        if (captchaCode.isPresent()) {
            if (!captchaCode.get().getCode().equals(registrationRequest.getCaptcha())) {
                errorResponse.put("captcha", "Код с картинки введён неверно");
            }
        } else errorResponse.put("captcha", "Код с картинки введён неверно или такого кода нет");

        RegistrationErrorResponse response = new RegistrationErrorResponse();
        if (errorResponse.isEmpty()) {
            response.setResult(true);
            User user = new User();
            user.setName(registrationRequest.getName());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(registrationRequest.getPassword());
            user.setRegTime(new Date());
            user.setModerator(false);
            userRepository.save(user);
            return response;
        }

        response.setErrors(errorResponse);
        response.setResult(false);
        return response;
    }
}
