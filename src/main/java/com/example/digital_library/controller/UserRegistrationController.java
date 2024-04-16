package com.example.digital_library.controller;

import com.example.digital_library.client.AuthenticationClient;
import com.example.digital_library.entity.User;
import com.example.digital_library.payload.request.loginAndSignup.LoginRequest;
import com.example.digital_library.payload.request.loginAndSignup.RegisterRequest;
import com.example.digital_library.payload.request.loginAndSignup.VerifyRequest;
import com.example.digital_library.payload.request.loginAndSignup.AuthenticationResponse;
import com.example.digital_library.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationController {

    private final AuthenticationClient authenticationClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationClient.registerUser(registerRequest);
        log.info("Received registration response: {} {}", responseEntity.getStatusCode(), responseEntity.getBody());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            User user = new User();
            user = User.builder()
                    .userFirstName(registerRequest.getFirstName().trim())
                    .userLastName(registerRequest.getLastName().trim())
                    .userEmail(registerRequest.getEmail())
                    .userPassword(passwordEncoder.encode(registerRequest.getPassword()))
                    .isVerified(false)
                    .role(registerRequest.getRole())
                    .build();
            userRepository.save(user);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }
        else {
               return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }

    }

    @PostMapping("/verify")
    ResponseEntity<AuthenticationResponse> verifyOtp(@Valid @RequestBody VerifyRequest verifyRequest) {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationClient.verifyOtp(verifyRequest);
        log.info("Received verification response: {} {}", responseEntity.getStatusCode(), responseEntity.getBody());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            User user = userRepository.findByUserEmail(verifyRequest.getEmail()).get();
            user.setIsVerified(true);
            userRepository.save(user);
        }
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUserEmail(loginRequest.getEmail()).orElse(null);
        if(user != null){
            ResponseEntity<AuthenticationResponse> responseEntity = authenticationClient.loginRequest(loginRequest);
            log.info("Received login response: {} {}", responseEntity.getStatusCode(), responseEntity.getBody());
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }
        else {
            return ResponseEntity.status(400).body(new AuthenticationResponse(null, "User not found"));
        }

    }

}
