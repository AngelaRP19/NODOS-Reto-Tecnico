package com.nodo.retotecnico.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "expansion_packs", schema = "---")
@AllArgsConstructor
@NoArgsConstructor
public class ExpansionPack {
    
    @Id
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
}

//Metodo get
public class ExpansionPacks {
    private Integer id;
    private String productName;
    private double price;



    //Constructor

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }
}