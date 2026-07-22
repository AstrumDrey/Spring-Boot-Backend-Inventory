package com.andrey.inventario.proyecto.services.product;

import java.math.BigDecimal;
import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrey.inventario.proyecto.dtos.ProductDTO.ProductRequest;
import com.andrey.inventario.proyecto.dtos.ProductDTO.ProductResponse;
import com.andrey.inventario.proyecto.globalExceptions.Exceptions.DuplicateResourceException;
import com.andrey.inventario.proyecto.globalExceptions.Exceptions.ResourceNotFoundException;
import com.andrey.inventario.proyecto.mapper.ProductMapper;
import com.andrey.inventario.proyecto.repository.Category.CategoryRepository;
import com.andrey.inventario.proyecto.repository.Product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minStock,
            Integer maxStock,
            Pageable pageable) {

        return productRepository.findByFilters(
                name,
                categoryId,
                minPrice,
                maxPrice,
                minStock,
                maxStock,
                pageable)
                .map(productMapper::EntityToDto);

    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The product with id:" + id + " is not found"));

        return productMapper.EntityToDto(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        if (productRepository.findByNameIgnoreCase(request.name()).isPresent()) {
            throw new DuplicateResourceException("The product already exists");
        }

        var categories = categoryRepository.findAllById(request.categoryIds());

        if (categories.size() != request.categoryIds().size()) {
            throw new ResourceNotFoundException("One or more categories not found");
        }

        var product = productMapper.DtoToEntity(request, new HashSet<>(categories));

        return productMapper.EntityToDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The product with id: " + id + "is not found"));

        if (!product.getName().equalsIgnoreCase(request.name())
                && productRepository.findByNameIgnoreCase(request.name()).isPresent()) {
            throw new DuplicateResourceException("Name already in use by another product");
        }

        var categories = categoryRepository.findAllById(request.categoryIds());

        if (categories.size() != request.categoryIds().size()) {
            throw new ResourceNotFoundException("One or more categories not found");
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setStock(request.stock());
        product.setStockMin(request.stockMin());
        product.setPrice(request.price());
        product.setCategories(new HashSet<>(categories));

        return productMapper.EntityToDto(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findLowStockProducts(Pageable pageable) {
        return productRepository.findLowStockProducts(pageable)
                .map(productMapper::EntityToDto);
    }

    @Transactional(readOnly = true)
    public long countLowStockProducts() {
        return productRepository.countLowStockProducts();
    }

    @Transactional
    public void deleteProduct(Long id) {

        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The product with id: " + id + "can't be found"));

        productRepository.delete(product);
    }
}

