package com.example.digital_library.controller;

import com.example.digital_library.entity.Loan;
import com.example.digital_library.payload.request.loan.CreateLoanRequest;
import com.example.digital_library.payload.response.ErrorResponse;
import com.example.digital_library.payload.response.loan.CreateLoanResponse;
import com.example.digital_library.payload.response.loan.ReturnBookResponse;
import com.example.digital_library.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
@Slf4j
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/createLoan")
    public ResponseEntity<?> createALoan(@Valid @RequestBody CreateLoanRequest createLoanRequest) {
        try {
            CreateLoanResponse response = loanService.createLoan(createLoanRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UsernameNotFoundException ex) {
            log.error(ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch(RuntimeException ex) {
            log.error(ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/returnBook/{loanId}")
    public ResponseEntity<?> returnBook(@PathVariable Integer loanId) {
        try {
            ReturnBookResponse response = loanService.returnBook(loanId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllLoans")
    public ResponseEntity<?> getAllLoans() {
        try {
            List<ReturnBookResponse> loans = loanService.getAllLoans();
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (DataAccessException ex) {
            log.error(ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getLoansByUserId/{userId}")
    public ResponseEntity<?> getLoansByUserId(@PathVariable Integer userId) {
        try {
            List<ReturnBookResponse> loans = loanService.getAllLoansByUser(userId);
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (DataAccessException ex) {
            log.error(ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorResponse(ex.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
