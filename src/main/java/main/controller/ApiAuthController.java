package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRequest;
import main.api.request.RegistrationRequest;
import main.api.response.CaptchaResponse;
import main.api.response.RegistrationErrorResponse;
import main.api.response.authCheckResponse.AuthCheckResponse;
import main.service.AuthCheckService;
import main.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {
    private final AuthCheckService authCheckService;
    private final CaptchaService captchaService;

    @GetMapping("/check")
    private ResponseEntity<AuthCheckResponse> authCheck(Principal principal) {
            return ResponseEntity.ok(authCheckService.getAuthCheckInfo(principal));
    }

    @GetMapping("/captcha")
    private ResponseEntity<CaptchaResponse> captcha() {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @PostMapping("/register")
    private ResponseEntity<RegistrationErrorResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authCheckService.getRegister(registrationRequest));
    }

    @PostMapping("/login")
    private ResponseEntity<AuthCheckResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authCheckService.login(loginRequest));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/logout")
    private ResponseEntity<AuthCheckResponse> logout() {
        return ResponseEntity.ok(authCheckService.logout());
    }
}

