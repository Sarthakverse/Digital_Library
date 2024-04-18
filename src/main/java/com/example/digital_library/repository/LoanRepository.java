package com.example.digital_library.repository;

import com.example.digital_library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer>{


    List<Loan> findAllByUserUserId(Integer userId);
}
