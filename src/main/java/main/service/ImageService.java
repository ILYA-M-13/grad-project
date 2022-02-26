package main.service;

import lombok.AllArgsConstructor;
import main.api.response.ErrorResponse;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {

    private final PostInfoService postInfoService;
    private final UserRepository userRepository;
    private final long MAX_SIZE = 2 * 1_024 * 1_024;


    public String loadImage(MultipartFile file) throws IOException {
        String path = getRandomPath(file.getName()).toString();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        file.transferTo(new File(path));
        return path;
    }

    public boolean checkImage(MultipartFile file) {
        if (file != null &&
                file.getOriginalFilename().toLowerCase(Locale.ROOT).matches("^(.*)(.)(png|jpg)$") &&
                file.getSize() <= MAX_SIZE) {
            return true;
        }
        return false;
    }

    public ErrorResponse errorLoad() {
        ErrorResponse errorResponse = new ErrorResponse();
        Map<String, String> errors = new HashMap<>();
        errors.put("image", "файл слишком большой или неверный формат");
        errorResponse.setResult(false);
        errorResponse.setErrors(errors);
        return errorResponse;
    }

    public StringBuilder getRandomPath(String fileName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        StringBuilder sb = new StringBuilder();
        sb.append("upload/")
                .append(uuid.substring(0, 5))
                .append("/").append(uuid.substring(5, 10))
                .append("/").append(uuid.substring(10, 15))
                .append(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
        return sb;
    }
}
