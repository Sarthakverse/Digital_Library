package com.example.digital_library.service;

import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.User;
import com.example.digital_library.payload.request.book.CreateBookRequest;
import com.example.digital_library.payload.response.book.CreateBookResponse;
import com.example.digital_library.repository.BookRepository;
import com.example.digital_library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CreateBookResponse createTheBook(CreateBookRequest createBookRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();

        String userEmail = user.getUserEmail();

        User loggedInUser = userRepository.findByUserEmail(userEmail).orElseThrow(
                ()-> new UsernameNotFoundException("LoggedInUser not found in the database with email"+userEmail)
        );


        if(loggedInUser.getRole().name().equals("ADMIN") || loggedInUser.getRole().name().equals("LIBRARIAN"))
        {
            log.info("loggedInUser is Admin/Librarian");
             Book book = new Book();
             book.setBookId(createBookRequest.getBookId());
             book.setBookName(createBookRequest.getBookName());
             book.setBookEdition(createBookRequest.getBookEdition());
             book.setPublicationYear(String.valueOf(createBookRequest.getPublicationYear()));
             book.setBookPrice(createBookRequest.getBookPrice());
             book.setBookQuantity(createBookRequest.getBookQuantity());
             book.setAuthor(createBookRequest.getAuthorId());

             if(bookRepository.findById(book.getBookId()).isPresent())
             {
                 log.info("book already exist with id: {}", book.getBookId());
                 return CreateBookResponse.builder()
                         .message("Book already exists")
                         .build();
             }
             else{
                 bookRepository.save(book);
                 log.info("Book has been successfully created");

                 return CreateBookResponse.builder()
                         .message("Book has been successfully created, its details are given below: ")
                         .bookId(book.getBookId())
                         .bookName(book.getBookName())
                         .bookEdition(book.getBookEdition())
                         .publicationYear(Year.parse(book.getPublicationYear()))
                         .bookPrice(book.getBookPrice())
                         .bookQuantity(book.getBookQuantity())
                         .authorId(book.getAuthor())
                         .build();
             }


        }
        else {
            log.info("loggedInUser is not Admin/Librarian");
            return CreateBookResponse.builder()
                    .message("Only Admin/Librarian can create book")
                    .build();
        }

    }
}
