package com.nodo.retotecnico.dto;

import java.util.List;

public class CartResponseDTO {
    private Integer id;
    private String status;
    private UserDTO user;
    private List<CartItemDTO> items;
    private Double total;

    // Constructor con todos los campos
    public CartResponseDTO(Integer id, String status, UserDTO user, List<CartItemDTO> items, Double total) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.items = items;
        this.total = total;
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
