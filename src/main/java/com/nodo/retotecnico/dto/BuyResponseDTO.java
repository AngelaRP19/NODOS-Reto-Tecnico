package com.nodo.retotecnico.dto;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.nodo.retotecnico.model.Buy;
import com.nodo.retotecnico.service.LocalizedContentService;

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

    public static BuyResponseDTO fromBuy(Buy buy, LocalizedContentService localizedContentService, Locale locale) {
        List<BuyItemDTO> items = buy.getCart().getDetails() != null ? buy.getCart().getDetails().stream()
                .map(details -> BuyItemDTO.fromCartDetails(details, localizedContentService, locale))
                .collect(Collectors.toList()) : List.of();

        return new BuyResponseDTO(
                buy.getId(),
                buy.getPurchaseDate(),
                buy.getTotalPrice(),
                buy.getPaymentMethod(),
                buy.getStatus(),
                items);
    }
}
