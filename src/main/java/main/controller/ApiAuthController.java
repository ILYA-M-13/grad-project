package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.RegistrationRequest;
import main.service.AuthCheckService;
import main.service.CaptchaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {
    private final AuthCheckService authCheckService;
    private final CaptchaService captchaService;

    @GetMapping("/check")
    private ResponseEntity authCheck() {
        return new ResponseEntity<>(authCheckService.getAuthCheckInfo(), HttpStatus.OK);
    }

    @GetMapping("/captcha")
    private ResponseEntity captcha() {
        return new ResponseEntity<>(captchaService.getCaptcha(), HttpStatus.OK);
    }

    @PostMapping("/register")
    private ResponseEntity register(@RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authCheckService.getRegister(registrationRequest), HttpStatus.OK);
    }
}
