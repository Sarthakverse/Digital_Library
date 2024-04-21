package com.example.digital_library.service;

import com.example.digital_library.entity.Author;
import com.example.digital_library.entity.Role;
import com.example.digital_library.entity.User;
import com.example.digital_library.payload.request.author.CreateAuthorRequest;
import com.example.digital_library.payload.response.author.CreateAuthorResponse;
import com.example.digital_library.payload.response.author.DeleteAuthorResponse;
import com.example.digital_library.repository.AuthorRepository;
import com.example.digital_library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTests {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        authorService = new AuthorService(userRepository, authorRepository);
    }

    @Test
    public void testAddAuthor_AdminUser_NewAuthor() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);

        // Mocking userRepository
        when(userRepository.findByUserEmail("sarthak@gmail.com")).thenReturn(Optional.of(adminUser));

        // Mocking authorRepository
        when(authorRepository.findByAuthorName("William Shakespeare")).thenReturn(Optional.empty());

        CreateAuthorResponse response = authorService.addAuthor(new CreateAuthorRequest("William Shakespeare", "NewYork, USA"));

        // Assertions
        assertEquals("Author has been successfully created", response.getMessage());
    }


    @Test
    public void testAddAuthor_NonAdminUser_Failure(){
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User nonAdminUser = new User();
        nonAdminUser.setUserEmail("ayush@gmail.com");
        nonAdminUser.setRole(Role.USER);

        when(authentication.getPrincipal()).thenReturn(nonAdminUser);

        when(userRepository.findByUserEmail("ayush@gmail.com")).thenReturn(Optional.of(nonAdminUser));

        CreateAuthorResponse response = authorService.addAuthor(new CreateAuthorRequest("William Shakespeare", "NewYork, USA"));

        // Assertions
        assertEquals("Only Admin can create author", response.getMessage());
    }

    @Test
    public void testAddAuthor_AdminUser_ExistingAuthor(){
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);

        when(userRepository.findByUserEmail("sarthak@gmail.com")).thenReturn(Optional.of(adminUser));
        when(authorRepository.findByAuthorName("William Shakespeare")).thenReturn(Optional.of(new Author()));

        CreateAuthorResponse response = authorService.addAuthor(new CreateAuthorRequest("William Shakespeare", "NewYork, USA"));

        assertEquals("Author already exists", response.getMessage()) ;
    }

    @Test
    public void testAddAuthor_AdminUser_LoggedInUserNotFound(){
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("naman@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);

        when(userRepository.findByUserEmail("naman@gmail.com")).thenReturn(Optional.empty());

        // expecting an exception to be thrown
        Exception exception = assertThrows(UsernameNotFoundException.class,()->{
        authorService.addAuthor(new CreateAuthorRequest("William Shakespeare", "NewYork, USA"));
        });

        // Assertions
        String expectedMessage = "LoggedInUser not found with email: naman@gmail.com";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    public void getAllAuthorsTests_foundAll()
    {
        Pageable pageable = PageRequest.of(0, 10);
        List<Author> authors = new ArrayList<>();
        Page<Author> authorPage = new PageImpl<>(authors,pageable, authors.size());

        when(authorRepository.findAll(pageable)).thenReturn(authorPage);

        Page<Author> result = authorService.getAllAuthors(pageable);

        assertEquals(authorPage, result,"The returned page should match the expected page");
    }

    @Test
    public void getAllAuthorsTests_error()
    {
        Pageable pageable = PageRequest.of(0,10);

        when(authorRepository.findAll(pageable)).thenThrow(new RuntimeException("An error occurred while fetching all authors"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authorService.getAllAuthors(pageable);
        });

        // Assertions
        String expectedMessage = "An error occurred while fetching all authors";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    public void deleteAuthorByIdTests_userIsAdmin_authorFound()
    {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);

        Author author = new Author();
        author.setAuthorId(1);
        when(authorRepository.findAuthorByAuthorId(1)).thenReturn(Optional.of(author));

        authorService.deleteAuthorById(1);

        verify(authorRepository,times(1)).delete(author);
    }

    @Test
    public void deleteAuthorByIdTests_userIsAdmin_authorNotFound() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User adminUser = new User();
        adminUser.setUserEmail("sarthak@gmail.com");
        adminUser.setRole(Role.ADMIN);

        when(authentication.getPrincipal()).thenReturn(adminUser);
        when(authorRepository.findAuthorByAuthorId(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authorService.deleteAuthorById(1);
        });
        String expectedMessage = "Author not found with id1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteAuthorByIdTests_userIsNotAdmin_USER() {
        Authentication authentication =mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User notAdminUser = new User();
        notAdminUser.setUserEmail("ayush@gmail.com");
        notAdminUser.setRole(Role.USER);

        when(authentication.getPrincipal()).thenReturn(notAdminUser);

        DeleteAuthorResponse response = authorService.deleteAuthorById(1);

        assertEquals("Only Admin can delete author", response.getMessage());
    }

    @Test
    public void deleteAuthorByIdTests_userIsNotAdmin_LIBRARIAN() {
        Authentication authentication =mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User notAdminUser = new User();
        notAdminUser.setUserEmail("ayush@gmail.com");
        notAdminUser.setRole(Role.LIBRARIAN);

        when(authentication.getPrincipal()).thenReturn(notAdminUser);

        DeleteAuthorResponse response = authorService.deleteAuthorById(1);

        assertEquals("Only Admin can delete author", response.getMessage());
    }
}

