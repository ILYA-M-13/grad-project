package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.github.cage.YCage;
import com.github.cage.image.EffectConfig;
import com.github.cage.image.Painter;
import lombok.AllArgsConstructor;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CaptchaService {
    private final long HOUR = 3_600_000;
    private final int CAPTCHA_LEN = 4;
    CaptchaRepository captchaRepository;

    public CaptchaResponse getCaptcha() {
        String secret = UUID.randomUUID().toString();
        Cage cage1 = new GCage();

        String code = genString(CAPTCHA_LEN);

        String image = "data:image/png;base64, " + Base64.getEncoder().encodeToString(cage1.draw(code));
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setSecretCode(secret);
        captchaCode.setCode(code);
        captchaCode.setTime(new Date());
        captchaRepository.save(captchaCode);
        return new CaptchaResponse(secret, image);
    }

    private String genString(int len) {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        String captcha = "";
        for (int i = 0; i < len; i++) {
            int r = new Random().nextInt(10);
            captcha += String.valueOf(letters[r]);
        }
        return captcha;
    }

    @Scheduled(fixedRate = 60000)
    public void deleteOldCaptcha() {
        captchaRepository.deleteAll(captchaRepository.findOld(HOUR));

    }
}
