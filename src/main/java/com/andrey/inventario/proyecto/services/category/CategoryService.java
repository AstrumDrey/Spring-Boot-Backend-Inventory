package com.andrey.inventario.proyecto.services.category;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrey.inventario.proyecto.dtos.CategoryDTO.CategoryRequest;
import com.andrey.inventario.proyecto.dtos.CategoryDTO.CategoryResponse;
import com.andrey.inventario.proyecto.globalExceptions.Exceptions.DuplicateResourceException;
import com.andrey.inventario.proyecto.globalExceptions.Exceptions.ResourceNotFoundException;
import com.andrey.inventario.proyecto.mapper.CategoryMapper;
import com.andrey.inventario.proyecto.repository.Category.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapperCategory;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        Map<Long, Integer> counts = categoryRepository.findProductCounts()
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).intValue()));

        return categoryRepository.findAll().stream()
                .map(cat -> mapperCategory.CategoryToDto(cat, counts.getOrDefault(cat.getId(), 0)))
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return categoryRepository.findById(id)
                .map(mapperCategory::CategoryToDto)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Category with id: " + id + "not found"));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.findByNameIgnoreCase(request.name()).isPresent()) {
            throw new DuplicateResourceException("Category " + request.name() + "alredy exists");
        }

        var category = mapperCategory.DtoToCategory(request);
        return mapperCategory.CategoryToDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id: " + id + " not found"));

        if (!category.getName().equalsIgnoreCase(request.name()) &&
                categoryRepository.findByNameIgnoreCase(request.name()).isPresent()) {
            throw new DuplicateResourceException("Name " + request.name() + " already in use");
        }

        category.setName(request.name());
        return mapperCategory.CategoryToDto(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id: " + id + " not found"));

        category.getProducts().forEach(p -> p.getCategories().remove(category));
        category.getProducts().clear();

        categoryRepository.delete(category);
    }
}
