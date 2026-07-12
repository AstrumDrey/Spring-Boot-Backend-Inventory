package com.andrey.inventario.proyecto.dtos.ProductDTO;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
                @NotBlank String name,
                String description,
                @NotNull @Min(0) Integer stock,
                @NotNull Integer stockMin,
                @NotNull @Positive BigDecimal price,
                @NotNull @NotEmpty Set<Long> categoryIds) {
}

/*
 * Los records en Java son clases inmutables que reemplazan el boilerplate de
 * una clase normal.
 * Es decir, no es necesario crear métodos get/set
 * constructores, toString, etc.
 *
 * Pero el inconveniente es que los datos son inmutables
 * por tanto no cambian (todos los campos se vuelven "final")
 */
