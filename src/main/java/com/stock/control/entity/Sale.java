package com.stock.control.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private Double finalPrice;

    @Column(length = 8)
    private String docId;

    @Column(name = "client", length = 120, nullable = false)
    private String clientFullName;

    @Column(nullable = false)
    private LocalDate dateOfSale;
}
