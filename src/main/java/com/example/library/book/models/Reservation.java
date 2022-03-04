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
public class Reservation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "client")
    private Client client;
    @ManyToMany(mappedBy = "reservations")
    private List<Book> books;
    @Temporal(TemporalType.DATE)
    private Date isReserved;
    @Temporal(TemporalType.DATE)
    private Date returnDate;
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Temporal(TemporalType.DATE)
    private Date reservationDate;

}
