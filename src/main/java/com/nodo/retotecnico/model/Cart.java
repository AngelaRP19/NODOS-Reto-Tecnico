package com.nodo.retotecnico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 🔥 CAMBIO AQUÍ
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relación con expansiones
    @ManyToMany
    @JoinTable(
        name = "cart_expansion",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "expansion_id")
    )
    private List<ExpansionPack> expansions = new ArrayList<>();

    public Cart() {}

    public Cart(User user) {
        this.user = user;
    }

    // MÉTODOS

    public void addExpansion(ExpansionPack expansion) {
        if (!this.expansions.contains(expansion)) {
            this.expansions.add(expansion);
        }
    }

    public void removeExpansion(ExpansionPack expansion) {
        this.expansions.remove(expansion);
    }

    public void clearCart() {
        this.expansions.clear();
    }

    public double getTotal() {
        return expansions.stream()
                .mapToDouble(ExpansionPack::getPrice)
                .sum();
    }
}