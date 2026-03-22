package com.nodo.retotecnico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Entity
@Table(name = "platforms")

@NoArgsConstructor
public class Platforms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;  // Ej. Xbox, PlayStation®
    private String url;   // URL de la tienda

    @OneToMany(mappedBy = "platform", cascade = CascadeType.ALL)
    private List<Product> products;
}

