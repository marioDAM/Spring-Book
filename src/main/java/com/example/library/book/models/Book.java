package com.example.library.book.models;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value="ID del libro", dataType="long",  example="1", position=1)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ApiModelProperty(value="Nombre del libro", dataType="String",  example="El Cantar del Mio Cid", position=2)
    private String name;
    @ApiModelProperty(value="Autor del libro", dataType="String",  example="Per Abbat", position=3)
    private String author;
    @ApiModelProperty(value="ISBN del libro", dataType="Integer",  example="45765-65-5", position=4)
    private Integer ISBN;
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "ISRESERVED", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isReserved;
    private String category;
    private String description;
    @ManyToMany
    private List<Reservation> reservations;


}
