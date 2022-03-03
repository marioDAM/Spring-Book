package com.example.library.book.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client")
    private Client client;
    @ManyToMany(mappedBy = "reservations")
    private List<Book> books;
    @Temporal(TemporalType.DATE)
    private Date fechaAlquilada;
    @Temporal(TemporalType.DATE)
    private Date fechaEntregada;
    @Temporal(TemporalType.DATE)
    private Date fechaLimite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
