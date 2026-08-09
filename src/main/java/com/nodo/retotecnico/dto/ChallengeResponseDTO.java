package com.nodo.retotecnico.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeResponseDTO {
    private Integer id;
    private String name;
    private LocalDate start;
    private LocalDate end;
    private String description;
    private String imageURL;
    private String language;
}
