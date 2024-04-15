package com.example.digital_library.repository;

import com.example.digital_library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Optional<Object> findByAuthorName(String authorName);

}
