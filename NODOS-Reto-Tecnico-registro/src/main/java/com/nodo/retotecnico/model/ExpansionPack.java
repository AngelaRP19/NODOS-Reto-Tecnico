package com.nodo.retotecnico.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
// removed lombok

@Entity
@Table(name = "expansion_packs")
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
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDistributor() { return distributor; }
    public void setDistributor(String distributor) { this.distributor = distributor; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPublicationDate() { return publicationDate; }
    public void setPublicationDate(String publicationDate) { this.publicationDate = publicationDate; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public List<Buy> getBuys() { return buys; }
    public void setBuys(List<Buy> buys) { this.buys = buys; }
}
