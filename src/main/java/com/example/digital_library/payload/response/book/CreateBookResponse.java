package com.example.digital_library.payload.response.book;

import com.example.digital_library.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookResponse {
    private String message;
    private Integer bookId;
    private String bookName;
    private String bookEdition;
    private Year publicationYear;
    private Double bookPrice;
    private Integer bookQuantity;
    private Author authorId;
}
