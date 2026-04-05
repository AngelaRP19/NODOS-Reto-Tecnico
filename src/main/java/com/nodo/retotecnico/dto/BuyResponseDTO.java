package com.nodo.retotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.nodo.retotecnico.model.Buy;
import com.nodo.retotecnico.model.CartDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyResponseDTO {
    private Integer id;
    private Date purchaseDate;
    private double totalPrice;
    private String paymentMethod;
    private String status;
    private List<BuyItemDTO> items;

    public static BuyResponseDTO fromBuy(Buy buy) {
        List<BuyItemDTO> items = buy.getCart().getDetails() != null ?
            buy.getCart().getDetails().stream()
                .map(BuyItemDTO::fromCartDetails)
                .collect(Collectors.toList()) : List.of();
        
        return new BuyResponseDTO(
            buy.getId(),
            buy.getPurchaseDate(),
            buy.getTotalPrice(),
            buy.getPaymentMethod(),
            buy.getStatus(),
            items
        );
    }
}
