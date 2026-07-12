package com.andrey.inventario.proyecto.mapper;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.andrey.inventario.proyecto.dtos.CategoryDTO.CategoryRequest;
import com.andrey.inventario.proyecto.dtos.CategoryDTO.CategoryResponse;
import com.andrey.inventario.proyecto.models.category.Category;

@Component
public class CategoryMapper {

    public Category DtoToCategory(CategoryRequest request) {

        var category = new Category(null, request.name(), new HashSet<>());
        return category;
    }

    public CategoryResponse CategoryToDto(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                0);
    }

    public CategoryResponse CategoryToDto(Category category, int productCount) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                productCount);
    }
}
