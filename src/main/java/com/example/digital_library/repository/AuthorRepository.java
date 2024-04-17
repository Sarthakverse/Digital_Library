package com.example.digital_library.repository;

import com.example.digital_library.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Optional<Author> findByAuthorName(String authorName);

    Optional<Author> findAuthorByAuthorId(Integer authorId);


    Optional<Author> findByAuthorNameAndAuthorAddress(String authorName, String authorAddress);
}
