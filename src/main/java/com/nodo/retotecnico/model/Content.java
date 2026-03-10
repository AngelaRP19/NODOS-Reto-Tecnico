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
