package com.example.digital_library.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_book_")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bookId")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookId;
    private String bookName;
    private Integer publicationYear;
    private Double bookPrice;
    private Integer bookQuantity;
    private String bookEdition;

    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", publicationYear=" + publicationYear +
                ", bookPrice=" + bookPrice +
                ", bookQuantity=" + bookQuantity +
                ", bookEdition='" + bookEdition + '\'' +
                '}';
    }
}
