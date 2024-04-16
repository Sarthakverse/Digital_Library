package com.example.digital_library.controller;

import com.example.digital_library.payload.request.book.CreateBookRequest;
import com.example.digital_library.payload.response.ErrorResponse;
import com.example.digital_library.payload.response.book.CreateBookResponse;
import com.example.digital_library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @PostMapping("/createBook")
    public ResponseEntity<?> createBook(@Valid @RequestBody CreateBookRequest createBookRequest) {

        try{
            CreateBookResponse response = bookService.createTheBook(createBookRequest);
            if(response.getMessage().equals("Book has been successfully created, its details are given below: "))
            {
                return new ResponseEntity<>(response,HttpStatus.CREATED);
            }
            else if(response.getMessage().equals("Book already exists"))
            {
                return new ResponseEntity<>(response,HttpStatus.CONFLICT);
            }
            else
            {
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

            }
        }
        catch(UsernameNotFoundException ex)
        {
            log.error("Error while creating book", ex);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setDetails(ex.getMessage());
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);

        }
    }

}
