package com.example.digital_library.service;

import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.Loan;
import com.example.digital_library.entity.User;
import com.example.digital_library.payload.request.loan.CreateLoanRequest;
import com.example.digital_library.payload.response.loan.CreateLoanResponse;
import com.example.digital_library.payload.response.loan.ReturnBookResponse;
import com.example.digital_library.repository.BookRepository;
import com.example.digital_library.repository.LoanRepository;
import com.example.digital_library.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Transactional
    public CreateLoanResponse createLoan(CreateLoanRequest createLoanRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        String userEmail = user.getUserEmail();

        User loggedInUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + userEmail));

        if (loggedInUser.getRole().name().equals("ADMIN") || loggedInUser.getRole().name().equals("LIBRARIAN")) {

            Book book = bookRepository.findById(createLoanRequest.getBook().getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " +
                            createLoanRequest.getBook().getBookId()));

            User requestedUser = userRepository.findById(createLoanRequest.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " +
                            createLoanRequest.getUser().getUserId()));

            if (book.getBookQuantity() > 0) {
                Loan loan = new Loan();
                loan.setBook(book);
                loan.setUser(requestedUser);
                loan.setStartDate(createLoanRequest.getStartDate());
                loan.setEndDate(createLoanRequest.getEndDate());
                loan.setIsReturned(createLoanRequest.getIsReturned());

                book.setBookQuantity(book.getBookQuantity() - 1);

                Loan savedLoan = loanRepository.save(loan);
                return CreateLoanResponse.builder()
                        .loanId(savedLoan.getLoanId())
                        .bookName(savedLoan.getBook().getBookName())
                        .userEmail(savedLoan.getUser().getUserEmail())
                        .startDate(String.valueOf(savedLoan.getStartDate()))
                        .endDate(String.valueOf(savedLoan.getEndDate()))
                        .isReturned(savedLoan.getIsReturned())
                        .build();
            } else {
                log.error("Book is not available.");
                throw new UsernameNotFoundException("Book is not available.");
            }

        } else {
            log.error("You are not allowed to approve loans.");
            throw new UsernameNotFoundException("You are not allowed to approve loans.");
        }
    }

    @Transactional
    public ReturnBookResponse returnBook(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new UsernameNotFoundException("Loan not found with ID: " + loanId));

        Book book = loan.getBook();
        if (loan.getIsReturned()) {
            log.error("Book is already returned.");
            throw new RuntimeException("Book is already returned.");
        } else {
            book.setBookQuantity(book.getBookQuantity() + 1);

            loan.setIsReturned(true);
            loanRepository.save(loan);
            return ReturnBookResponse.builder()
                    .loanId(loan.getLoanId())
                    .bookName(loan.getBook().getBookName())
                    .userEmail(loan.getUser().getUserEmail())
                    .startDate(String.valueOf(loan.getStartDate()))
                    .endDate(String.valueOf(loan.getEndDate()))
                    .isReturned(loan.getIsReturned())
                    .build();
        }

    }


    public List<ReturnBookResponse> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        List<ReturnBookResponse> returnBookResponses = new ArrayList<>();
        for (Loan loan : loans) {
            ReturnBookResponse response = new ReturnBookResponse();
            response.setLoanId(loan.getLoanId());
            response.setBookName(loan.getBook().getBookName());
            response.setUserEmail(loan.getUser().getUserEmail());
            response.setStartDate(String.valueOf(loan.getStartDate()));
            response.setEndDate(String.valueOf(loan.getEndDate()));
            response.setIsReturned(loan.getIsReturned());
            returnBookResponses.add(response);
        }
        return returnBookResponses;
    }

    public List<ReturnBookResponse> getAllLoansByUser(Integer userId) {
        List<Loan> loans = loanRepository.findAllByUserUserId(userId);
        List<ReturnBookResponse> returnBookResponses = new ArrayList<>();
        for (Loan loan : loans) {
            ReturnBookResponse response = new ReturnBookResponse();
            response.setLoanId(loan.getLoanId());
            response.setBookName(loan.getBook().getBookName());
            response.setUserEmail(loan.getUser().getUserEmail());
            response.setStartDate(String.valueOf(loan.getStartDate()));
            response.setEndDate(String.valueOf(loan.getEndDate()));
            response.setIsReturned(loan.getIsReturned());
            returnBookResponses.add(response);
        }
        return returnBookResponses;
    }
}
