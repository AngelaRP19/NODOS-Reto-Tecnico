package com.nodo.retotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.nodo.retotecnico.model.CartDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyItemDTO {
    private Integer id;
    private Integer quantity;
    private ExpansionPackDTO expansionPack;
    private PlatformDTO platform;

    public static BuyItemDTO fromCartDetails(CartDetails details) {
        return new BuyItemDTO(
            details.getId(),
            details.getQuantity(),
            new ExpansionPackDTO(
                details.getExpansionPack().getId(),
                details.getExpansionPack().getName(),
                details.getExpansionPack().getDescription(),
                details.getExpansionPack().getPrice()
            ),
            new PlatformDTO(
                details.getPlatform().getId(),
                details.getPlatform().getName()
            )
        );
    }
}
