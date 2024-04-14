package com.example.digital_library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_book_")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;
    private String bookName;
    private String publicationYear;
    private Double bookPrice;
    private Integer bookQuantity;
    private String bookEdition;

    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "book")
    private Set<Loan> loans = new HashSet<>();

}
