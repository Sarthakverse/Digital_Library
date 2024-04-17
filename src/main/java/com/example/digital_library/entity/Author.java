package com.example.digital_library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "_author_")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "authorId")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer authorId;
    private String  authorName;
    private String authorAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author",fetch = FetchType.EAGER)
    //@JsonBackReference
    private List<Book> book = new ArrayList<>();
    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", authorAddress='" + authorAddress + '\'' +
                '}';
    }



}
