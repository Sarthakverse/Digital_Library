package com.example.digital_library.controller;

import com.example.digital_library.entity.Book;
import com.example.digital_library.payload.request.book.CreateBookRequest;
import com.example.digital_library.payload.response.ErrorResponse;
import com.example.digital_library.payload.response.book.CreateBookResponse;
import com.example.digital_library.payload.response.book.DeleteBookResponse;
import com.example.digital_library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

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
    public ResponseEntity<?> getAllBooks(@RequestParam(defaultValue = "0")int page,
                                         @RequestParam(defaultValue = "10") int size)
    {
        try{
            Pageable pageable = (Pageable) PageRequest.of(page,size);
            Page<CreateBookResponse> bookPage = bookService.getAllBooks(pageable);
            return new ResponseEntity<>(bookPage.getContent(), HttpStatus.OK);
        }catch(Exception ex)
        {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooksByAuthor/{authorId}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable Integer authorId) {
        try {
            List<CreateBookResponse> books = bookService.getBooksByAuthor(authorId);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBook/{bookId}")
    public ResponseEntity<?> deleteBookById(@PathVariable Integer bookId)
    {
        try{
            DeleteBookResponse response = bookService.deleteBookById(bookId);
            if(response.getMessage().equals("Only Admin can delete book"))
            {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(UsernameNotFoundException ex)
        {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


}
