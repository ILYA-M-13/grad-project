package main.service;

import lombok.AllArgsConstructor;
import main.api.response.ErrorResponse;
import main.model.User;
import main.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageAndProfileService {

    public static final PasswordEncoder BCRYPT = new BCryptPasswordEncoder(12);

    private final PostInfoService postInfoService;
    private final UserRepository userRepository;
    private final long MAX_SIZE = 5 * 1_024 * 1_024;
    private final int IMAGE_MAX_WIDTH = 300;
    private final int PHOTO_MAX_WIDTH = 36;
    private final int PHOTO_MAX_HEIGHT = 36;
    private final int MIN_LENGTH_PAS = 6;

    public ErrorResponse editMyProfileWithPhoto(MultipartFile photo, boolean removePhoto,
                                                String name, String email, String password,
                                                Principal principal) throws IOException {
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

        if (photo.getSize() > MAX_SIZE) {
            errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
        }

        if (removePhoto) {
            user.setPhoto(null);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        if (errors.isEmpty()) {

            BufferedImage bufferedImage = ImageIO.read(photo.getInputStream());
            String ext = FilenameUtils.getExtension(photo.getOriginalFilename());
            String pathToPhoto = "/upload/" + user.getId() + "/" + photo.getOriginalFilename();
            Path path = Paths.get(pathToPhoto);
            resize(bufferedImage, PHOTO_MAX_WIDTH, PHOTO_MAX_HEIGHT, path, ext);

            user.setPhoto(pathToPhoto);
            userRepository.save(user);
            errorResponse.setResult(true);
            return errorResponse;
        }
        errorResponse.setErrors(errors);
        return errorResponse;
    }

    public String loadImage(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        Path path = getRandomPath(file.getOriginalFilename());

        if (bufferedImage.getWidth() > IMAGE_MAX_WIDTH) {
            int gettingHeight = (int) (bufferedImage.getWidth() / (double) IMAGE_MAX_WIDTH);
            int newHeight = Math.round(bufferedImage.getHeight() / gettingHeight);
            resize(bufferedImage, IMAGE_MAX_WIDTH, newHeight, path, ext);
        } else {
            if (!path.toFile().exists()) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            ImageIO.write(bufferedImage, ext, path.toFile());
        }
        return "/"+path.toString().replaceAll("\\\\", "/");
    }

    private void resize(BufferedImage bufferedImage, int width, int height, Path path, String ext) throws IOException {
        if (!path.toFile().exists()) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        BufferedImage scalrImage = Scalr.resize(bufferedImage, Scalr.Method.QUALITY, width, height);
        ImageIO.write(scalrImage, ext, path.toFile());
    }

    public Path getRandomPath(String fileName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        StringBuilder sb = new StringBuilder();
        sb.append("upload/")
                .append(uuid.substring(0, 3))
                .append("/").append(uuid.substring(3, 6))
                .append("/").append(uuid.substring(6, 9))
                .append('/').append(uuid.substring(10, 13))
                .append(fileName.substring(fileName.lastIndexOf(".")));

        return Paths.get(sb.toString());
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
        errorResponse.setErrors(errors);
        return errorResponse;
    }
}
