package com.example.digital_library.controller;

import com.example.digital_library.client.AuthenticationClient;
import com.example.digital_library.payload.request.LoginRequest;
import com.example.digital_library.payload.request.RegisterRequest;
import com.example.digital_library.payload.request.VerifyRequest;
import com.example.digital_library.payload.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Slf4j
public class UserRegistrationController {

    private final AuthenticationClient authenticationClient;
    public UserRegistrationController(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
    }

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationClient.registerUser(registerRequest);
        log.info("Received registration response: {} {}", responseEntity.getStatusCode(), responseEntity.getBody());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

    @PostMapping("/verify")
    ResponseEntity<AuthenticationResponse> verifyOtp(@Valid @RequestBody VerifyRequest verifyRequest) {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationClient.verifyOtp(verifyRequest);
        log.info("Received verification response: {} {}", responseEntity.getStatusCode(), responseEntity.getBody());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationClient.loginRequest(loginRequest);
        log.info("Received login response: {} {}", responseEntity.getStatusCode(), responseEntity.getBody());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

}
