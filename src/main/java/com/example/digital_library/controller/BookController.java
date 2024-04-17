package com.example.digital_library.controller;

import com.example.digital_library.entity.Book;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<?> createBook(@Valid @RequestBody CreateBookRequest createBookRequest)
    {
        try{
            CreateBookResponse response = bookService.createTheBook(createBookRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch(UsernameNotFoundException ex)
        {
            log.error(ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        catch(IllegalArgumentException | IllegalStateException ex)
        {
            log.error(ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        } catch(Exception ex)
        {
            log.error("internal server error occurred");
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getBookById/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Integer bookId)
    {
       try{
           Book book = bookService.getBookById(bookId);
           return new ResponseEntity<>(book, HttpStatus.OK);
       }
       catch(UsernameNotFoundException ex)
       {
           log.error(ex.getMessage());
              ErrorResponse errorResponse = ErrorResponse.builder()
                     .errorResponse(ex.getMessage())
                     .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
       }

    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<?> getAllBooks()
    {
        try{
            return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
        }catch(Exception ex)
        {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooksByAuthor/{authorId}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable Integer authorId)
    {
        try{
            return new ResponseEntity<>(bookService.getBooksByAuthor(authorId), HttpStatus.OK);
        }catch(Exception ex)
        {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
