package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.AllArgsConstructor;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
        Cage cage = new GCage();
        String code = cage.getTokenGenerator().next();
        String image = "data:image/png;base64, " + Base64.getEncoder().encodeToString(cage.draw(code));
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
