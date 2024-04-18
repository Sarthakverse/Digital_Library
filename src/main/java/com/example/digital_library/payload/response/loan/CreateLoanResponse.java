package com.example.digital_library.payload.response.loan;

import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanResponse {
    private Integer loanId;
    private String startDate;
    private String endDate;
    private Boolean isReturned;
    private String bookName;
    private String userEmail;
}
