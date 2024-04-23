package com.example.digital_library.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "_loan_")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "loanId")
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer loanId;

    private Date startDate;
    private Date endDate;
    private Boolean isReturned;

    @ManyToOne(cascade = CascadeType.ALL)
//    @JsonManagedReference
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
