package com.example.digital_library.payload.request.loan;

import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanRequest implements Serializable {
    @NotNull(message = "Start date is required")
    private Date startDate;
    @NotNull(message = "End date is required")
    private Date endDate;
    @NotNull(message = "Book is required")
    private Book book;
    @NotNull(message = "User is required")
    private User user;
    @NotNull(message = "isReturned is required")
    private Boolean isReturned;
}
