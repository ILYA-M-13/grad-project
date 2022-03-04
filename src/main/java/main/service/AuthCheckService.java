package main.service;

import lombok.AllArgsConstructor;
import main.api.request.*;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.*;


@Service
@AllArgsConstructor
public class AuthCheckService {

    public static final PasswordEncoder BCRYPT = new BCryptPasswordEncoder(12);

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final AuthenticationManager authenticationManager;
    private final ConverterService converterService;
    private final MailService mailService;
    private final PostInfoService postInfoService;
    private final int MIN_LENGTH_PAS = 6;


    public ErrorResponse editMyProfile(EditProfileRequest profileRequest, Principal principal) {
        String name = profileRequest.getName();
        String email = profileRequest.getEmail();
        String password = profileRequest.getPassword();
        boolean removePhoto = profileRequest.isRemovePhoto();

        User user = postInfoService.getAuthUser(principal);
        Map<String, String> errors = new HashMap<>();

        if (!name.equals(user.getName())) {
            if (!name.matches("[А-Яа-яA-Za-z-]+([А-Яа-яA-Za-z-\\s]+)?")) {
                errors.put("name", "Имя указано неверно");
            } else user.setName(name);
        }

        if (!email.equals(user.getEmail())) {
            if (userRepository.findByEmail(email).isPresent()) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            } else user.setEmail(email);
        }

        if (password != null) {
            if (password.length() < MIN_LENGTH_PAS) {
                errors.put("password", "Пароль короче 6-ти символов");
            } else user.setPassword(BCRYPT.encode(password));
        }

        if (removePhoto) {
            user.setPhoto(null);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        if (errors.isEmpty()) {
            userRepository.save(user);
            errorResponse.setResult(true);
            return errorResponse;
        }
        errorResponse.setErrors(errors);
        return errorResponse;
    }

    public ErrorResponse getRegistration(RegistrationRequest registrationRequest) {
        Optional<CaptchaCode> captchaCode =
                captchaRepository.findCaptchaCodeBySecretCode(registrationRequest.getCaptchaSecret());
        Iterable<User> users = userRepository.findAll();
        Map<String, String> errors = new HashMap<>();

        users.forEach(u -> {
            if (u.getEmail().equalsIgnoreCase(registrationRequest.getEmail())) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            }
        });
        if (!registrationRequest.getName().matches("[А-Яа-яA-Za-z-]+([А-Яа-яA-Za-z-\\s]+)?")) {
            errors.put("name", "Имя указано неверно");
        }
        if (registrationRequest.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (captchaCode.isPresent()) {
            if (!captchaCode.get().getCode().equals(registrationRequest.getCaptcha())) {
                errors.put("captcha", "Код с картинки введён неверно");
            }
        } else errors.put("captcha", "Код с картинки введён неверно или такого кода нет");

        ErrorResponse response = new ErrorResponse();
        if (errors.isEmpty()) {
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

        response.setErrors(errors);
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

    public ErrorResponse getRestorePassword(PassRestoreRequest pass) {
        String login = pass.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(login);
        if (userOptional.isEmpty()) {
            return new ErrorResponse();
        }
        User user = userOptional.get();
        String HASH = UUID.randomUUID().toString().replaceAll("-", "");
        user.setCode(HASH);
        userRepository.save(user);
        String URL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String email = URL + "/login/change-password/" + HASH;
        mailService.send(login, "change-password", email);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResult(true);
        return errorResponse;
    }

    public ErrorResponse getChangePassword(PassChangeRequest pass) {
        String requestCode = pass.getCode();
        String requestCaptcha = pass.getCaptcha();
        String requestSecretCaptcha = pass.getCaptchaSecret();
        String requestPassword = pass.getPassword();
        Map<String, String> errors = new HashMap<>();
        Optional<User> optionalUser = userRepository.findByCode(requestCode);
        if (optionalUser.isEmpty()) {
            errors.put("code",
                    "Ссылка для восстановления пароля устарела.<a href=\"/auth/restore\">Запросить ссылку снова</a>");
        }
        Optional<CaptchaCode> optionalCaptcha = captchaRepository.findCaptchaCodeBySecretCode(requestSecretCaptcha);
        if (optionalCaptcha.isEmpty() || !optionalCaptcha.get().getCode().equals(requestCaptcha)) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        if (requestPassword.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        ErrorResponse errorResponse = new ErrorResponse();
        if (errors.isEmpty()) {
            User user = optionalUser.get();
            user.setPassword(BCRYPT.encode(requestPassword));
            userRepository.save(user);
            errorResponse.setResult(true);
            return errorResponse;
        } else errorResponse.setErrors(errors);
        errorResponse.setResult(false);
        return errorResponse;
    }
}
