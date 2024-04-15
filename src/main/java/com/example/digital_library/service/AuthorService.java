package com.example.digital_library.service;

import com.example.digital_library.entity.Author;
import com.example.digital_library.entity.User;
import com.example.digital_library.exceptions.UserNotFoundException;
import com.example.digital_library.payload.request.author.CreateAuthorRequest;
import com.example.digital_library.payload.response.author.CreateAuthorResponse;
import com.example.digital_library.repository.AuthorRepository;
import com.example.digital_library.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;

//-----------------------------create author API---------------------------------------------------------------------------------

    @Transactional
    public CreateAuthorResponse createAuthor(CreateAuthorRequest createAuthorRequest)
    {
        log.info("successfully received CreateAuthorRequest: {} {}", createAuthorRequest.getAuthorName(), createAuthorRequest.getAuthorAddress());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 = (User) authentication.getPrincipal();

        String userEmail = user1.getUserEmail();


        User loggedInUser = userRepository.findByUserEmail(userEmail).orElseThrow(
                ()-> new UserNotFoundException("loggedInUser not found")
        );


        if(loggedInUser.getRole().name().equals("ADMIN"))
        {
            //create an author
            log.info("loggedInUser is Admin");
            Author author  = new Author();
            author.setAuthorName(createAuthorRequest.getAuthorName().trim());
            author.setAuthorAddress(createAuthorRequest.getAuthorAddress().trim());

            // if author already exists
            if(authorRepository.findByAuthorName(author.getAuthorName()).isPresent())
            {
                log.info("Author already exists");
                return CreateAuthorResponse.builder()
                        .message("Author already exists")
                        .build();
            }

            authorRepository.save(author);
            log.info("Admin successfully created new author: {} {}", author.getAuthorName(), author.getAuthorAddress());

            return CreateAuthorResponse.builder()
                    .message("Author has been successfully created")
                    .authorId(author.getAuthorId())
                    .authorName(author.getAuthorName())
                    .authorAddress(author.getAuthorAddress())
                    .build();
        }
        else {
            log.info("loggedInUser is not Admin");
            return CreateAuthorResponse.builder()
                    .message("Only Admin can create author")
                    .build();
        }
    }
}
