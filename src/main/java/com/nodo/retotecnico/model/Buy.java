package com.nodo.retotecnico.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "buys")
@AllArgsConstructor
@NoArgsConstructor
public class Buy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;    
    private Date purchaseDate;
    private double totalPrice;
    private String paymentMethod;
    private String status;
    private Boolean deleted = false;

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
}
