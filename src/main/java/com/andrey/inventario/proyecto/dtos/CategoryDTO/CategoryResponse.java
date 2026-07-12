package com.andrey.inventario.proyecto.dtos.CategoryDTO;

public record CategoryResponse(
        Long id,
        String name,
        int productCount) {
}
