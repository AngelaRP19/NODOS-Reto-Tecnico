package com.nodo.retotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO utilizado para mostrar las plataformas disponibles antes de agregar una expansión al carrito.*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformSelectionDTO {

    private String name;

    private String label;

}