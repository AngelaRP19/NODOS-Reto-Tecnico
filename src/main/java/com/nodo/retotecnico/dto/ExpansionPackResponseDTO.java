package com.nodo.retotecnico.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String URLImage;

    private List<String> characteristics;
    private List<String> screenshots;
    private List<String> minimumRequirements;
    private List<String> recommendedRequirements;

    @JsonProperty("URLImage")
    public String getURLImage() {
        return URLImage;
    }

    @JsonProperty("URLImage")
    public void setURLImage(String URLImage) {
        this.URLImage = URLImage;
    }
}
