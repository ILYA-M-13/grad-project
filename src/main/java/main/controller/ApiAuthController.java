package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRequest;
import main.api.request.PassChangeRequest;
import main.api.request.PassRestoreRequest;
import main.api.request.RegistrationRequest;
import main.api.response.CaptchaResponse;
import main.api.response.ErrorResponse;
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
    public ResponseEntity<AuthCheckResponse> authCheck(Principal principal) {
        return ResponseEntity.ok(authCheckService.getAuthCheckInfo(principal));
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> captcha() {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @PostMapping("/register")
    public ResponseEntity<ErrorResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authCheckService.getRegistration(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthCheckResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authCheckService.login(loginRequest));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/logout")
    public ResponseEntity<AuthCheckResponse> logout() {
        return ResponseEntity.ok(authCheckService.logout());
    }

    @PostMapping("/restore")
    public ResponseEntity<ErrorResponse> restorePassword(@RequestBody PassRestoreRequest pass) {
        return ResponseEntity.ok(authCheckService.getRestorePassword(pass));
    }

    @PostMapping("/password")
    public ResponseEntity<ErrorResponse> changePassword(@RequestBody PassChangeRequest pass) {
        return ResponseEntity.ok(authCheckService.getChangePassword(pass));
    }
}

