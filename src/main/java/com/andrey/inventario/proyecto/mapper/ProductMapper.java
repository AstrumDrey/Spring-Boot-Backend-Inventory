package com.andrey.inventario.proyecto.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.andrey.inventario.proyecto.dtos.CategoryDTO.CategoryResponse;
import com.andrey.inventario.proyecto.dtos.ProductDTO.ProductRequest;
import com.andrey.inventario.proyecto.dtos.ProductDTO.ProductResponse;
import com.andrey.inventario.proyecto.models.category.Category;
import com.andrey.inventario.proyecto.models.product.Product;

@Component
public class ProductMapper {

    public Product DtoToEntity(ProductRequest request, Set<Category> categories) {

        var producto = new Product(
                null,
                request.name(), request.description(), request.stock(), request.stockMin(), request.price(), null,
                categories);

        return producto;
    }

    public ProductResponse EntityToDto(Product product) {

        var productResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getStockMin(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getCategories().stream()
                        .map(c -> new CategoryResponse(c.getId(), c.getName(), 0))
                        .collect(Collectors.toSet())

        );

        return productResponse;

    }
}
