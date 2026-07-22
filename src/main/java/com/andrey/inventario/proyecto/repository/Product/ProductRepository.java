package com.andrey.inventario.proyecto.repository.Product;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andrey.inventario.proyecto.models.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


  @Query("""
      SELECT DISTINCT p FROM Product p
      LEFT JOIN p.categories c
      WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:name AS text), '%')))
        AND (:categoryId IS NULL OR c.id = :categoryId)
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (:minStock IS NULL OR p.stock >= :minStock)
        AND (:maxStock IS NULL OR p.stock <= :maxStock)
      """)
  Page<Product> findByFilters(@Param("name") String name,
      @Param("categoryId") Long categoryId,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("minStock") Integer minStock,
      @Param("maxStock") Integer maxStock,
      Pageable pageable);

  Optional<Product> findByNameIgnoreCase(String name);

  @Query("SELECT p FROM Product p WHERE p.stock <= p.stockMin")
  Page<Product> findLowStockProducts(Pageable pageable);

  @Query("SELECT COUNT(p) FROM Product p WHERE p.stock <= p.stockMin")
  long countLowStockProducts();

}
