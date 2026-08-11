package com.nodo.retotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionChallengeResponseDTO {
    private Integer id;
    private Integer userId;
    private ChallengeResponseDTO challenge;
    private String status;
}
