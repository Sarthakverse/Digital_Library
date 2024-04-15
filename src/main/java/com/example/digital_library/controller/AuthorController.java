package com.example.digital_library.controller;

import com.example.digital_library.payload.request.author.CreateAuthorRequest;
import com.example.digital_library.payload.response.author.CreateAuthorResponse;
import com.example.digital_library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/create")
    public ResponseEntity<CreateAuthorResponse> createAuthor(@Valid @RequestBody CreateAuthorRequest createAuthorRequest) {
        CreateAuthorResponse response = authorService.createAuthor(createAuthorRequest);
        if(response.getMessage().equals("Author has been successfully created")) {
            return ResponseEntity.ok(response);
        }
        else if(response.getMessage().equals("Author already exists")) {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
