package com.nodo.retotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectBuyRequest {
    private Integer expansionId;
    private Integer platformId;
    private String paymentMethod;
}
