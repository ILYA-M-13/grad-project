package main.service;

import lombok.AllArgsConstructor;
import main.api.request.LoginRequest;
import main.api.request.RegistrationRequest;
import main.api.response.ErrorResponse;
import main.api.response.authCheckResponse.AuthCheckResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthCheckService {

    public static final PasswordEncoder BCRYPT = new BCryptPasswordEncoder(12);

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final AuthenticationManager authenticationManager;
    private final ConverterService converterService;

    public ErrorResponse getRegistration(RegistrationRequest registrationRequest) {
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

        ErrorResponse response = new ErrorResponse();
        if (errorResponse.isEmpty()) {
            response.setResult(true);
            User user = new User();
            user.setName(registrationRequest.getName());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(BCRYPT.encode(registrationRequest.getPassword()));
            user.setRegTime(new Date());
            user.setModerator(false);
            userRepository.save(user);
            return response;
        }

        response.setErrors(errorResponse);
        response.setResult(false);
        return response;
    }

    public AuthCheckResponse getAuthCheckInfo(Principal principal) {
        return principal == null ? new AuthCheckResponse() : getAuthCheckResponse(principal.getName());
    }

    public AuthCheckResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        return getAuthCheckResponse(user.getUsername());
    }

    public AuthCheckResponse logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        AuthCheckResponse authCheckResponse = new AuthCheckResponse();
        authCheckResponse.setResult(true);
        return authCheckResponse;
    }

    private AuthCheckResponse getAuthCheckResponse(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        AuthCheckResponse authCheckResponse = new AuthCheckResponse();
        authCheckResponse.setResult(true);
        authCheckResponse.setUser(converterService.convertAuthChekUser(user));
        return authCheckResponse;
    }
}
