package com.nodo.retotecnico.dto;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.nodo.retotecnico.model.CartDetails;
import com.nodo.retotecnico.service.LocalizedContentService;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyItemDTO {
    private Integer id;
    private Integer quantity;
    private ExpansionPackResponseDTO expansionPack;
    private PlatformDTO platform;

    public static BuyItemDTO fromCartDetails(CartDetails details, LocalizedContentService localizedContentService,
            Locale locale) {
        return new BuyItemDTO(
                details.getId(),
                details.getQuantity(),
                localizedContentService.toResponseDto(details.getExpansionPack(), locale),
                new PlatformDTO(
                        details.getPlatform().getId(),
                        details.getPlatform().getName()));
    }
}
