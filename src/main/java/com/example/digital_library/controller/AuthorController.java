package com.example.digital_library.controller;

import com.example.digital_library.entity.Author;

import com.example.digital_library.payload.request.author.CreateAuthorRequest;
import com.example.digital_library.payload.response.ErrorResponse;
import com.example.digital_library.payload.response.author.CreateAuthorResponse;
import com.example.digital_library.payload.response.author.DeleteAuthorResponse;
import com.example.digital_library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/author")
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/createAuthor")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody CreateAuthorRequest createAuthorRequest) {

        try {
            CreateAuthorResponse response = authorService.addAuthor(createAuthorRequest);
            if (response.getMessage().equals("Author has been successfully created")) {
                return ResponseEntity.ok(response);
            } else if (response.getMessage().equals("Author already exists")) {
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        catch(UsernameNotFoundException ex)
        {
            log.error("Error while creating author", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

   @GetMapping("/getAllAuthors")
    public ResponseEntity<?> getAllTheAuthors(@RequestParam(defaultValue = "0")int page,
                                              @RequestParam(defaultValue = "10")int size)
   {
       try{
           Pageable pageable = (Pageable) PageRequest.of(page,size);
           Page<Author> authorPage = authorService.getAllAuthors(pageable);
           return new ResponseEntity<>(authorPage.getContent(), HttpStatus.OK);
       }catch(RuntimeException ex)
       {
           log.error("Error while fetching all authors", ex);
           return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }

   }

    @GetMapping("/getAuthorById/{authorId}")
//    @Cacheable(value = "author", key = "#authorId")
    public Author getAuthorById(@PathVariable Integer authorId) {
        try {
            Author author = authorService.getAuthorByID(authorId);
//            author.getBook().size();
            return author;
        } catch (UsernameNotFoundException ex) {
            log.error("Error while fetching author by id", ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

//   @CacheEvict(value = "author", key = "#authorId")
   @DeleteMapping("/deleteAuthor/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Integer authorId)
   {
       try {
           DeleteAuthorResponse response = authorService.deleteAuthorById(authorId);
           if (response.getMessage().equals("Only Admin can delete author"))
           {
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
           }

           return new ResponseEntity<>(response, HttpStatus.OK);
       }
       catch(UsernameNotFoundException ex)
       {
           log.error("user not found to delete");
           ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
       }

   }
}
