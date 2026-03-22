package com.nodo.retotecnico.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "expansion_packs")
@NoArgsConstructor
@AllArgsConstructor
public class ExpansionPack {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private String description;
    private String distributor;
    private double price;
    private String category;
    private String publicationDate;
    private String language;

   
    @OneToMany(mappedBy = "expansionPack", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Buy> buys;

    @JsonIgnore
    @ManyToMany(mappedBy = "expansions")
    private List<Cart> carts;
}