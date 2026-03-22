package com.nodo.retotecnico.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;        // Nombre del producto
    private double price;       // Precio del producto
    private Date releaseDate;   // Fecha de lanzamiento (opcional)

    // Relación ManyToOne con Platforms
    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platforms platform;
}