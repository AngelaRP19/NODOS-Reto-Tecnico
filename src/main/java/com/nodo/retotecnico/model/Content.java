package com.nodo.retotecnico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contents", schema = "---")
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    
    @Id
    private Integer id;
    private String section;
    private String title;
    private String description;
    private String image;

}
//Metodo get

public class Content {
    private Integer id;
    private String productName;
    private double price;
}

//Constructores

public long getId() {
    return id;
}

public String getProductName() {
    return ProductName;
}

public double getPrice() {
    return price;
}