package com.nodo.retotecnico.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private Integer cartId;
    private String status;
    private UserDTO user;
    private List<CartItemDTO> items;
}
