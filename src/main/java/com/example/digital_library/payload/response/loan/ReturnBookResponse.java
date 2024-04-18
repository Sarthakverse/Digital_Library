package com.example.digital_library.payload.response.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBookResponse {
    private Integer loanId;
    private String bookName;
    private String userEmail;
    private String startDate;
    private String endDate;
    private Boolean isReturned;
}
