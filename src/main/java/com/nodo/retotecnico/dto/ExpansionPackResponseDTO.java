package com.nodo.retotecnico.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpansionPackResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private String platforms;
    private Double price;
    private String category;
    private String publicationDate;
    private String language;
    private String URLImage;
    private List<String> characteristics;
}
