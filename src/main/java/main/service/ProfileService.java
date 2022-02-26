package main.service;

import lombok.AllArgsConstructor;
import main.api.request.PassRestoreRequest;
import main.api.response.ErrorResponse;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final MailService mailService;

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
        String URL =  ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String email = URL+"/login/change-password/" + HASH;
        mailService.send(login,"change-password",email);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResult(true);
        return errorResponse;
    }
}
