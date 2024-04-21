package com.example.digital_library.service;

import com.example.digital_library.entity.Author;
import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.Role;
import com.example.digital_library.entity.User;
import com.example.digital_library.payload.request.book.CreateBookRequest;
import com.example.digital_library.payload.response.book.CreateBookResponse;
import com.example.digital_library.payload.response.book.DeleteBookResponse;
import com.example.digital_library.repository.AuthorRepository;
import com.example.digital_library.repository.BookRepository;
import com.example.digital_library.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @InjectMocks
    private BookService bookService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        bookService = new BookService(userRepository, bookRepository, authorRepository);
    }

    @Test
    public void createTheBookTests_userIsAdmin_successfulBookCreation() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);
        when(userRepository.findByUserEmail("sarthak@gmail.com")).thenReturn(Optional.of(adminUser));
        when(bookRepository.findByBookName("The Alchemist")).thenReturn(Optional.empty());

        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setBookName("The Alchemist");
        createBookRequest.setBookEdition("1st");
        createBookRequest.setPublicationYear(1988);
        createBookRequest.setBookPrice(500.00);
        createBookRequest.setBookQuantity(10);

        Author author = new Author();
        author.setAuthorId(1);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        createBookRequest.setAuthor(author);

        Book savedBook = new Book();
        savedBook.setBookName("The Alchemist");
        savedBook.setBookEdition("1st");
        savedBook.setPublicationYear(1988);
        savedBook.setBookPrice(500.00);
        savedBook.setBookQuantity(10);
        savedBook.setAuthor(author);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        CreateBookResponse response = bookService.createTheBook(createBookRequest);

        verify(userRepository, times(1)).findByUserEmail("sarthak@gmail.com");
        verify(bookRepository, times(1)).findByBookName("The Alchemist");
        verify(authorRepository, times(1)).findById(1);
        verify(bookRepository, times(1)).save(any(Book.class));


        assertNotNull(response);
        assertEquals("The Alchemist", response.getBookName());
        assertEquals("1st", response.getBookEdition());
        assertEquals(1988, response.getPublicationYear());
        assertEquals(500.00, response.getBookPrice());
        assertEquals(10, response.getBookQuantity());
        assertEquals(author.getAuthorName(), response.getAuthor());
    }

    @Test
    public void createTheBookTests_userIsLibrarian_successfulBookCreation() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);
        when(userRepository.findByUserEmail("sarthak@gmail.com")).thenReturn(Optional.of(adminUser));
        when(bookRepository.findByBookName("The Alchemist")).thenReturn(Optional.empty());

        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setBookName("The Alchemist");
        createBookRequest.setBookEdition("1st");
        createBookRequest.setPublicationYear(1988);
        createBookRequest.setBookPrice(500.00);
        createBookRequest.setBookQuantity(10);

        Author author = new Author();
        author.setAuthorId(1);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        createBookRequest.setAuthor(author);

        Book savedBook = new Book();
        savedBook.setBookName("The Alchemist");
        savedBook.setBookEdition("1st");
        savedBook.setPublicationYear(1988);
        savedBook.setBookPrice(500.00);
        savedBook.setBookQuantity(10);
        savedBook.setAuthor(author);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        CreateBookResponse response = bookService.createTheBook(createBookRequest);

        verify(userRepository, times(1)).findByUserEmail("sarthak@gmail.com");
        verify(bookRepository, times(1)).findByBookName("The Alchemist");
        verify(authorRepository, times(1)).findById(1);
        verify(bookRepository, times(1)).save(any(Book.class));


        assertNotNull(response);
        assertEquals("The Alchemist", response.getBookName());
        assertEquals("1st", response.getBookEdition());
        assertEquals(1988, response.getPublicationYear());
        assertEquals(500.00, response.getBookPrice());
        assertEquals(10, response.getBookQuantity());
        assertEquals(author.getAuthorName(), response.getAuthor());
    }


    @Test
    public void createTheBookTests_userIsNotAdmin_throwsIllegalStateException() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User isNotAdmin = new User();
        isNotAdmin.setUserEmail("ayush@gmail.com");
        isNotAdmin.setRole(Role.USER);

        when(authentication.getPrincipal()).thenReturn(isNotAdmin);
        when(userRepository.findByUserEmail("ayush@gmail.com")).thenReturn(Optional.of(isNotAdmin));
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setBookName("The Alchemist");
        createBookRequest.setBookEdition("1st");
        createBookRequest.setPublicationYear(1988);
        createBookRequest.setBookPrice(500.00);
        createBookRequest.setBookQuantity(10);
        Author author = new Author();
        author.setAuthorId(1);
        lenient().when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        createBookRequest.setAuthor(author);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            bookService.createTheBook(createBookRequest);
        });

        verify(userRepository, times(1)).findByUserEmail("ayush@gmail.com");
        assertEquals("You are not authorized to create a book", exception.getMessage());

    }

    @Test
    public void createTheBookTests_bookAlreadyExists_throwsIllegalArgumentException() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);
        when(userRepository.findByUserEmail("sarthak@gmail.com")).thenReturn(Optional.of(adminUser));
        when(bookRepository.findByBookName("The Alchemist")).thenReturn(Optional.of(new Book()));
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setBookName("The Alchemist");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.createTheBook(createBookRequest);
        });

        verify(userRepository, times(1)).findByUserEmail("sarthak@gmail.com");
        assertEquals("Book with the same name already exists", exception.getMessage());

    }

    @Test
    public void getBookById_bookExists_returnBook() {
        Book book = new Book();
        book.setBookId(1);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book fetchedBook = bookService.getBookById(1);

        verify(bookRepository, times(1)).findById(1);
        assertNotNull(fetchedBook);
        assertEquals(1, fetchedBook.getBookId());

    }

    @Test
    public void getBookById_bookDoesNotExist_throwsUsernameNotFoundException() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            bookService.getBookById(1);
        });

        verify(bookRepository, times(1)).findById(1);
        assertEquals("Book with id 1 not found", exception.getMessage());
    }

    @Test
    public void getAllBooksTests_Found() {

        Pageable pageable = PageRequest.of(0, 10);

        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setBookId(1);
        book1.setBookName("Book1");
        books.add(book1);

        Book book2 = new Book();
        book2.setBookId(2);
        book2.setBookName("Book2");
        books.add(book2);

        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<CreateBookResponse> result = bookService.getAllBooks(pageable);

        verify(bookRepository, times(1)).findAll(pageable);

        assertEquals(bookPage.getTotalElements(), result.getTotalElements(), "The total elements should match");
        assertEquals(bookPage.getSize(), result.getSize(), "The size should match");
        assertEquals(bookPage.getNumber(), result.getNumber(), "The page number should match");
    }

    @Test
    public void getBooksByAuthorIdTest_success() {
        Author author = new Author();
        author.setAuthorId(1);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setBookId(1);
        book1.setBookName("Book1");
        book1.setAuthor(author);

        Book book2 = new Book();
        book2.setBookId(2);
        book2.setBookName("Book2");
        book2.setAuthor(author);

        books.add(book1);
        books.add(book2);

        when(bookRepository.findBookByAuthor_AuthorId(1)).thenReturn(books);

        List<CreateBookResponse> result = bookService.getBooksByAuthor(1);

        verify(authorRepository, times(1)).findById(1);
        verify(bookRepository, times(1)).findBookByAuthor_AuthorId(1);

        assertEquals(2, result.size());

    }

    @Test
    public void getBooksByAuthorIdTest_failure() {
        Integer authorId = 1;


        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            bookService.getBooksByAuthor(authorId);
        });


        assertEquals("Author with id " + authorId + " not found", exception.getMessage());
    }

    @Test
    public void deleteBookById_bookExists_success() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);

        Book book = new Book();
        book.setBookId(1);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        DeleteBookResponse response = bookService.deleteBookById(1);

        verify(bookRepository, times(1)).findById(1);
        verify(bookRepository, times(1)).delete(book);

        assertNotNull(response);
    }


    @Test
    public void deleteBookById_bookDoesNotExist_throwsUsernameNotFoundException() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);

        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            bookService.deleteBookById(1);
        });

        verify(bookRepository, times(1)).findById(1);
        assertEquals("Book not found with id1", exception.getMessage());


    }

    @Test
    public void deleteBookById_userIsNotAdmin_throwsIllegalStateException() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User isNotAdmin = new User();
        isNotAdmin.setUserEmail("ayush@gmail.com");
        isNotAdmin.setRole(Role.USER);

        when(authentication.getPrincipal()).thenReturn(isNotAdmin);

        DeleteBookResponse response = bookService.deleteBookById(1);

        assertNotNull(response);
        assertEquals("Only Admin can delete book", response.getMessage());
    }
}
