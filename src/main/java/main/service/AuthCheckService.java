package main.service;

import main.api.response.authCheckResponse.AuthCheckResponseDTO;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthCheckService {

    UserRepository userRepository;

    @Autowired
    public AuthCheckService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthCheckResponseDTO getAuthCheckInfo() {
        AuthCheckResponseDTO authCheckResponse = new AuthCheckResponseDTO();

        return authCheckResponse;
    }
}
