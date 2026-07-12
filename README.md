# Inventario Proyecto

API RESTful para gestión de inventario de productos y categorías, construida con **Java 21 + Spring Boot 4.1**.

## Propósito

Sistema backend para administrar un inventario de productos con categorización múltiple. Permite crear, listar (con filtros y paginación), actualizar y eliminar productos y categorías, incluyendo detección de stock bajo.

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Spring Data JPA / Hibernate | — |
| Spring Validation | — |
| PostgreSQL | — |
| Lombok | — |
| Maven | — |

## Estructura del proyecto

```
src/main/java/com/andrey/inventario/proyecto/
├── Application.java
├── controller/
│   ├── Product/ProductController.java
│   └── Category/CategoryController.java
├── models/
│   ├── product/Product.java
│   └── category/Category.java
├── repository/
│   ├── Product/ProductRepository.java
│   └── Category/CategoryRepository.java
├── services/
│   ├── product/ProductService.java
│   └── category/CategoryService.java
├── dtos/
│   ├── ProductDTO/ProductRequest.java
│   ├── ProductDTO/ProductResponse.java
│   ├── CategoryDTO/CategoryRequest.java
│   └── CategoryDTO/CategoryResponse.java
├── mapper/
│   ├── ProductMapper.java
│   └── CategoryMapper.java
└── globalExceptions/
    ├── GlobalExceptionHandler.java
    └── Exceptions/
        ├── ResourceNotFoundException.java
        └── DuplicateResourceException.java
```

## Endpoints

### Productos — `/api/v1/products`

| Método | Ruta | Descripción | Parámetros query |
|---|---|---|---|
| `GET` | `/api/v1/products` | Listar productos paginados con filtros | `name`, `categoryId`, `minPrice`, `maxPrice`, `minStock`, `maxStock`, `page`, `size`, `sort` |
| `GET` | `/api/v1/products/{id}` | Obtener producto por ID | — |
| `GET` | `/api/v1/products/low-stock` | Productos con stock ≤ stockMin | `page`, `size` |
| `GET` | `/api/v1/products/low-stock/count` | Conteo de productos con stock bajo | — |
| `POST` | `/api/v1/products` | Crear producto | Body: `ProductRequest` |
| `PUT` | `/api/v1/products/{id}` | Actualizar producto | Body: `ProductRequest` |
| `DELETE` | `/api/v1/products/{id}` | Eliminar producto | — |

### Categorías — `/api/v1/categories`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/categories` | Listar todas las categorías |
| `GET` | `/api/v1/categories/{id}` | Obtener categoría por ID |
| `POST` | `/api/v1/categories` | Crear categoría |
| `PUT` | `/api/v1/categories/{id}` | Actualizar categoría |
| `DELETE` | `/api/v1/categories/{id}` | Eliminar categoría |

## DTOs

### ProductRequest
```json
{
  "name": "Laptop Gaming",
  "description": "Laptop con RTX 4070",
  "stock": 10,
  "stockMin": 3,
  "price": 1200.00,
  "categoryIds": [1, 2, 3]
}
```

### ProductResponse
```json
{
  "id": 1,
  "name": "Laptop Gaming",
  "description": "Laptop con RTX 4070",
  "stock": 10,
  "stockMin": 3,
  "price": 1200.00,
  "createdAt": "2026-07-09T12:00:00",
  "categories": [
    { "id": 1, "name": "Electrónica", "productCount": 0 }
  ]
}
```

### CategoryRequest
```json
{ "name": "Electrónica" }
```

### CategoryResponse
```json
{ "id": 1, "name": "Electrónica", "productCount": 5 }
```

## Códigos de respuesta

| Código | Significado |
|---|---|
| `200 OK` | Operación exitosa (GET, PUT) |
| `201 Created` | Recurso creado (POST) |
| `204 No Content` | Recurso eliminado (DELETE) |
| `400 Bad Request` | Error de validación |
| `404 Not Found` | Recurso no encontrado |
| `409 Conflict` | Recurso duplicado |

## Cómo ejecutar

```bash
# Requisito: Java 21+ y Maven

# Desarrollo
./mvnw spring-boot:run

# La API corre en:
# http://localhost:8080
```

## Configuración

Toda la configuración está en `src/main/resources/application.properties`:

```properties
spring.application.name=inventario.proyecto
spring.datasource.url=${DATABASE_URL}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.open-in-view=false
```

La variable `DATABASE_URL` debe ser inyectada como variable de entorno del sistema o configurada en la plataforma de despliegue (Render, Railway, etc.).

### Ejemplo de variable de entorno

```bash
DATABASE_URL="jdbc:postgresql://ep-xxx-pooler.us-east-2.aws.neon.tech/neondb?user=user&password=pass&sslmode=require"
```

## Licencia

CC BY-NC 4.0 — Creative Commons Attribution-NonCommercial 4.0 International.
Puedes ver, copiar y modificar el código, pero no usarlo con fines comerciales.
