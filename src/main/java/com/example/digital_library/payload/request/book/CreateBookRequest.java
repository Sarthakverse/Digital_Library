package com.example.digital_library.payload.request.book;

import com.example.digital_library.entity.Author;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @NotBlank(message = "Book name is required")
    private String bookName;

    @NotBlank(message = "Book edition is required")
    private String bookEdition;

    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2024, message = "Publication year must be at most 2024")
    private Integer publicationYear;

    @Min(value = 1, message = "Book price must be at least 1")
    private Double bookPrice;

    @Min(value = 1, message = "Book quantity must be at least 1")
    private Integer bookQuantity;

    @NotNull(message = "AuthorId is required")
    private Author author;

}
