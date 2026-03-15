package com.nodo.retotecnico.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})} )
public class User {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)

    Integer id;
    @Column(nullable = false)
    String username;
    String lastname;
    String firstname;
    String country;
    String email;
    String password;
    @Enumerated(EnumType.STRING)
    com.nodo.retotecnico.Users.Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
       return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpire(){
       return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
}
