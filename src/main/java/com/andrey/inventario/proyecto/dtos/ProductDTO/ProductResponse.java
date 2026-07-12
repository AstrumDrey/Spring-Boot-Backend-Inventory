package com.andrey.inventario.proyecto.dtos.ProductDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.andrey.inventario.proyecto.dtos.CategoryDTO.CategoryResponse;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Integer stock,
        Integer stockMin,
        BigDecimal price,
        LocalDateTime createdAt,
        Set<CategoryResponse> categories

) {
}
