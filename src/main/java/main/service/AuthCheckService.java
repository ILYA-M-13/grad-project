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

//        Optional<User> optionalUser = userRepository.findById(id);
//        AuthCheckResponse.User user = new AuthCheckResponse.User();
//        user.setId(optionalUser.get().getId());
//        user.setName(optionalUser.get().getName());
//        user.setPhoto(optionalUser.get().getPhoto());
//        user.setEmail(optionalUser.get().getEmail());
//        if (!optionalUser.get().isModerator()) {
//            user.setModeration(false);
//            user.setModerationCount(0);
//        }

        return authCheckResponse;
    }
}
