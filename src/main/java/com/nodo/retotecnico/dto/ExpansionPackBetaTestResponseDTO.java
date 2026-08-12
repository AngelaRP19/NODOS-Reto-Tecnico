package com.nodo.retotecnico.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpansionPackBetaTestResponseDTO {
    private Integer id;
    private Integer userId;
    private ExpansionPackResponseDTO expansionPack;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String feedback;
}
