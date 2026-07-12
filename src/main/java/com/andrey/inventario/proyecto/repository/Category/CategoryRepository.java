package com.andrey.inventario.proyecto.repository.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.andrey.inventario.proyecto.models.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    @Query("SELECT c.id, COUNT(p) FROM Category c LEFT JOIN c.products p GROUP BY c.id")
    List<Object[]> findProductCounts();
}
