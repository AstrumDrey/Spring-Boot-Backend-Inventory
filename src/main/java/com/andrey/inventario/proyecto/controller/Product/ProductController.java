package com.andrey.inventario.proyecto.controller.Product;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andrey.inventario.proyecto.dtos.ProductDTO.ProductRequest;
import com.andrey.inventario.proyecto.dtos.ProductDTO.ProductResponse;
import com.andrey.inventario.proyecto.services.product.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor // Activa inyección por constructor (mejor práctica)
public class ProductController {

    private final ProductService productService;

    /*
     * @RequestParam vincula parámetros de la URL (query string) a los parámetros
     * del método del controlador.
     *
     * Diferencias con otras anotaciones:
     *
     * @RequestBody → Toma los datos del CUERPO de la petición (POST/PUT)
     * Ej: { "name": "Laptop", "price": 1200 }
     *
     * @PathVariable → Toma un valor de la RUTA de la URL
     * Ej: /api/v1/products/{id} → @PathVariable Long id
     *
     * @RequestParam → Toma valores de la QUERY STRING (después del ?)
     * Ej: /api/v1/products?name=Laptop&categoryId=3
     *
     * Nuestro endpoint actual con ejemplos de uso:
     *
     * 1. SIN FILTROS (solo paginación):
     * GET /api/v1/products?page=0&size=10
     *
     * 2. FILTRANDO POR NOMBRE:
     * GET /api/v1/products?name=laptop&page=0&size=10
     *
     * 3. FILTRANDO POR NOMBRE + CATEGORÍA:
     * GET /api/v1/products?name=laptop&categoryId=3&page=0&size=10
     *
     * 4. FILTRANDO POR RANGO DE PRECIO:
     * GET /api/v1/products?minPrice=100&maxPrice=2000&page=0&size=10
     *
     * 5. FILTRANDO POR STOCK MÍNIMO:
     * GET /api/v1/products?minStock=5&page=0&size=10
     *
     * 6. CON ORDENACIÓN:
     * GET /api/v1/products?name=laptop&page=0&size=10&sort=price,desc
     *
     * @RequestParam(required = false) → el parámetro es opcional
     * Si no se envía, el valor del parámetro Java es null
     * y la query JPQL lo ignora con "IS NULL"
     */

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @Min(0) BigDecimal minPrice,
            @RequestParam(required = false) @Min(0) BigDecimal maxPrice,
            @RequestParam(required = false) @Min(0) Integer minStock,
            @RequestParam(required = false) @Min(0) Integer maxStock,
            Pageable pageable

    ) {
        return ResponseEntity.ok(
                productService.findAll(name, categoryId, minPrice, maxPrice, minStock, maxStock, pageable));
        // ResponseEntity.ok equivale => ResponseEntity.status().body()
    }

    @GetMapping("/low-stock")
    public ResponseEntity<Page<ProductResponse>> getLowStockProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.findLowStockProducts(pageable));
    }

    @GetMapping("/low-stock/count")
    public ResponseEntity<Map<String, Long>> getLowStockCount() {
        long count = productService.countLowStockProducts();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        var product = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
