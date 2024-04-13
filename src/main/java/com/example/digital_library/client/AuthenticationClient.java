package com.example.digital_library.client;

import com.example.digital_library.constants.Constant;
import com.example.digital_library.entity.User;
import com.example.digital_library.payload.request.LoginRequest;
import com.example.digital_library.payload.request.RegisterRequest;
import com.example.digital_library.payload.request.VerifyRequest;
import com.example.digital_library.payload.response.AuthenticationResponse;
import com.example.digital_library.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationClient {

    String baseUrl = Constant.BASE_URL;
    private final RestTemplate restTemplate;

//---------------SENDING_REGISTER_REQUEST_TO_AUTHENTICATION_SERVER-------------------------------------------------------------------------------------------------------------------
    public ResponseEntity<AuthenticationResponse> registerUser(RegisterRequest registerRequest)
    {
        String url = baseUrl + Constant.REGISTER_ENDPOINT;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> registerRequestHttpEntity = new HttpEntity<>(registerRequest, headers);
        System.out.println("Register Request: " + registerRequest);

        ResponseEntity<AuthenticationResponse> registerResponseHttpEntity = restTemplate.exchange(url,HttpMethod.POST,registerRequestHttpEntity,AuthenticationResponse.class);
        log.info("Sent registration request Successfully: {} {}", registerResponseHttpEntity.getStatusCode(), registerResponseHttpEntity.getBody());
        return registerResponseHttpEntity;
    }

//---------------VERIFYING_OTP_FROM_AUTHENTICATION_SERVER--------------------------------------------------------------------------------------------------------------------------
   public ResponseEntity<AuthenticationResponse> verifyOtp(VerifyRequest verifyRequest)
   {
       String url = baseUrl + Constant.VERIFY_ENDPOINT;
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<VerifyRequest> verifyRequestHttpEntity = new HttpEntity<>(verifyRequest , headers);

       ResponseEntity<AuthenticationResponse> verifyResponseHttpEntity = restTemplate.exchange(url , HttpMethod.POST, verifyRequestHttpEntity, AuthenticationResponse.class);

       log.info("Successfully sent verification Request: {} {}", verifyResponseHttpEntity.getStatusCode(), verifyResponseHttpEntity.getBody());

       return verifyResponseHttpEntity;
   }

//-------------LOGIN_REQUEST_TO_AUTHENTICATION_SERVER----------------------------------------------------------------------------------------------------------------------------
  public ResponseEntity<AuthenticationResponse> loginRequest(LoginRequest loginRequest)
  {
      String url = baseUrl + Constant.LOGIN_ENDPOINT;
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<LoginRequest> loginRequestHttpEntity = new HttpEntity<>(loginRequest , headers);
      ResponseEntity<AuthenticationResponse> loginResponseHttpEntity = restTemplate.exchange(url, HttpMethod.POST,loginRequestHttpEntity, AuthenticationResponse.class);

      log.info("Successfully sent login request: {} {}", loginResponseHttpEntity.getStatusCode(), loginResponseHttpEntity.getBody());

      return loginResponseHttpEntity;
  }








}
