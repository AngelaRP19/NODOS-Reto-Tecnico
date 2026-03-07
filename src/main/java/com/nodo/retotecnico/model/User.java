package com.nodo.retotecnico.model;

import java.util.Date;
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
@Table(name = "users", schema = "---")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    private Integer id;
    private String name;
    private String lastName;
    private String email;
    private Date registrationDate;
    private String role;
    private String nickname;
    private String country;
    private String username;
    private String password; //debe estar encriptada

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Buy> buys;

}
