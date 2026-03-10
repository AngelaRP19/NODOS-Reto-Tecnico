package com.nodo.retotecnico.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "buys", schema = "---")
@AllArgsConstructor
@NoArgsConstructor
public class Buy {
    
    //Hacer etiquetas de acuerdo con la base de datos

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)//nombre de acuerdo a la entidad Users
    private User user;

    @ManyToOne
    @JoinColumn(name = "expansion_pack_id", nullable = false)
    private ExpansionPack expansionPack;
    private Date purchaseDate;
    private double totalPrice;
    private String paymentMethod;
    private String status;
}
