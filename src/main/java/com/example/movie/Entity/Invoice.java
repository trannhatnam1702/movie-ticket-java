package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;


@Data
@Entity
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total")
    private Double total;

    @Column(name = "created_at")
    private Date createdAt;

    public void setCreatedAt() {
        this.createdAt = new Date(System.currentTimeMillis());
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    Set<Reservation> reservations;

}
