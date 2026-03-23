package com.nodo.retotecnico.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String status; // "activo", "inactivo", "comprado" 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartDetails> details;

    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Buy buy;


    // MÉTODOS

    public void addExpansion(ExpansionPack expansion) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        // Verificar si la expansión ya existe
        boolean exists = this.details.stream()
                .anyMatch(detail -> detail.getExpansionPack().equals(expansion));
        
        if (!exists) {
            CartDetails cartDetail = new CartDetails();
            cartDetail.setCart(this);
            cartDetail.setExpansionPack(expansion);
            this.details.add(cartDetail);
        }
    }

    public void removeExpansion(ExpansionPack expansion) {
        if (this.details != null) {
            this.details.removeIf(detail -> detail.getExpansionPack().equals(expansion));
        }
    }

    public void clearCart() {
        if (this.details != null) {
            this.details.clear();
        }
    }

    public double getTotal() {
        if (this.details == null || this.details.isEmpty()) {
            return 0.0;
        }
        return this.details.stream()
                .mapToDouble(detail -> detail.getExpansionPack().getPrice())
                .sum();
    }
}