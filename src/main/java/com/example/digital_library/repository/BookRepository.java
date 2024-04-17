package com.example.digital_library.repository;

import com.example.digital_library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer>{

    Optional<Book> findByBookName(String bookName);

    List<Book> findBookByAuthor_AuthorId(Integer author_authorId);
}
