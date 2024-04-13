package com.example.digital_library.exceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public Map<String, String> handleConflictErrorException(HttpClientErrorException ex) {
        return getStringStringMap(ex);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        Map<String, String> errorMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) ->{
            errorMap.put(error.getField(), error.getDefaultMessage());

        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public Map<String, String> handleBadRequestErrorException(HttpClientErrorException ex) {
        return getStringStringMap(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public Map<String, String> handleNotFoundErrorException(HttpClientErrorException ex) {
        return getStringStringMap(ex);
    }



    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public Map<String, String> handleUnauthorizedErrorException(HttpClientErrorException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(ex.getResponseBodyAsString());
            JsonNode messageNode = jsonNode.get("message");
            if (messageNode != null)
                errorMap.put("message", messageNode.asText());
            else {
                errorMap.put("message", "user is not registered");
            }

        } catch (JsonProcessingException e) {
            errorMap.put("message", "Error parsing response body");
        }
        return errorMap;
    }

    private Map<String, String> getStringStringMap(HttpClientErrorException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(ex.getResponseBodyAsString());
            errorMap.put("message", jsonNode.get("message").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return errorMap;
    }


}

