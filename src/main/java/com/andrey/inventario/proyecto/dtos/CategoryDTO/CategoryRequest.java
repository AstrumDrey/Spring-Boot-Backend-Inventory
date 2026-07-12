package com.andrey.inventario.proyecto.dtos.CategoryDTO;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank String name) {

}
