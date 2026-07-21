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

  // Podemos usar Query methods() para encontrar filtros de manera sencilla y
  // simple.

  // Con "Specifications" podemos crear un filtrado mucho más avanzado.
  // Solo hay que extenderlo en el repository y Spring se encarga también.

  /*
   * Page<T> es un objeto de Spring Data JPA que se usa junto con Pageable.
   * Cuando un método del Repository recibe Pageable como parámetro,
   * Spring devuelve automáticamente Page<T> en lugar de List<T>.
   *
   * Page<T> contiene:
   * - getContent(): la lista de elementos de la página actual
   * - getTotalElements(): total de registros en BD (sin paginar)
   * - getTotalPages(): número total de páginas
   * - getNumber(): página actual (empieza en 0)
   * - getSize(): elementos por página
   * - getSort(): criterio de ordenación
   * - isFirst() / isLast(): si es primera o última página
   * - isEmpty(): si no hay resultados
   *
   * El cliente controla la paginación con query params en la URL:
   * GET /api/v1/products?page=0&size=10&sort=name,asc
   *
   * Ventajas frente a List<T>:
   * - El frontend sabe cuántas páginas tiene para mostrar la navegación
   * - Evita cargar miles de registros en una sola petición
   * - Spring parsea page, size y sort automáticamente desde la URL
   */

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
