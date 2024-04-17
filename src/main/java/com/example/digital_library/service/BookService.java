package com.example.digital_library.service;
import com.example.digital_library.entity.Author;
import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.User;
import com.example.digital_library.payload.request.book.CreateBookRequest;
import com.example.digital_library.payload.response.book.CreateBookResponse;
import com.example.digital_library.repository.AuthorRepository;
import com.example.digital_library.repository.BookRepository;
import com.example.digital_library.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public CreateBookResponse createTheBook(CreateBookRequest createBookRequest)
    {
        log.info("Creating a book: {}", createBookRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getUserEmail();

        User loggedInUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + userEmail));

        if (loggedInUser.getRole().name().equals("ADMIN") || loggedInUser.getRole().name().equals("LIBRARIAN")) {
            Book existingBook = bookRepository.findByBookName(createBookRequest.getBookName()).orElse(null);

            if (existingBook == null) {
                Book book = new Book();
                book.setBookName(createBookRequest.getBookName());
                book.setBookEdition(createBookRequest.getBookEdition());
                book.setPublicationYear(createBookRequest.getPublicationYear());
                book.setBookPrice(createBookRequest.getBookPrice());
                book.setBookQuantity(createBookRequest.getBookQuantity());
                Author author = authorRepository.findById(createBookRequest.getAuthor().getAuthorId()).orElseThrow(
                        ()-> new UsernameNotFoundException("Author with id "+createBookRequest.getAuthor().getAuthorId()+" not found")
                );
                book.setAuthor(author);
                Book savedBook = bookRepository.save(book);

                log.info("Book created successfully: {}", savedBook);

                return CreateBookResponse.builder()
                        .message("book created successfully and its details are:-")
                        .bookId(savedBook.getBookId())
                        .bookName(savedBook.getBookName())
                        .bookEdition(savedBook.getBookEdition())
                        .publicationYear(savedBook.getPublicationYear())
                        .bookPrice(savedBook.getBookPrice())
                        .bookQuantity(savedBook.getBookQuantity())
                        .author(savedBook.getAuthor())
                        .build();
            } else {
                throw new IllegalArgumentException("Book with the same name already exists");
            }
        } else {
            throw new IllegalStateException("You are not authorized to create a book");
        }

    }


    @Transactional
    public Book getBookById(Integer bookId)
    {
        log.info("Fetching book by id: {}", bookId);
        return bookRepository.findById(bookId).orElseThrow(
                () -> new UsernameNotFoundException("Book with id "+bookId+" not found")
        );
    }

    @Transactional
    public List<Book> getAllBooks()
    {
            log.info("Fetching all books");
            return bookRepository.findAll();

    }

    @Transactional
    public List<Book> getBooksByAuthor(Integer authorId)
    {
        log.info("Fetching all books by author id: {}", authorId);
        return bookRepository.findBookByAuthor_AuthorId(authorId);
    }
}
