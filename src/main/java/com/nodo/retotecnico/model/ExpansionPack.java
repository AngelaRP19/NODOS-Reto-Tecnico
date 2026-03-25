package com.nodo.retotecnico.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    private List<CartDetails> cartDetails;
}