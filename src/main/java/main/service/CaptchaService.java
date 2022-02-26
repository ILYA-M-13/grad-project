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
import java.util.UUID;

@Service
@AllArgsConstructor
public class CaptchaService {
    private final long HOUR = 3_600_000;
    CaptchaRepository captchaRepository;

    public CaptchaResponse getCaptcha() {
        String secret = UUID.randomUUID().toString();
        Cage cage1 = new GCage();

        String code = cage1.getTokenGenerator().next();
        String image = "data:image/png;base64, " + Base64.getEncoder().encodeToString(cage1.draw(code));
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setSecretCode(secret);
        captchaCode.setCode(code);
        captchaCode.setTime(new Date());
        captchaRepository.save(captchaCode);
        return new CaptchaResponse(secret,image);
    }
    @Scheduled(fixedRate = 60000)
    public void deleteOldCaptcha(){
        captchaRepository.deleteAll(captchaRepository.findOld(HOUR));

    }
}
