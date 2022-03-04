package com.example.library.book.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String author;
    private Integer ISBN;
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "ISRESERVED", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isReserved;
    @ManyToMany
    private List<Reservation> reservations;


}
