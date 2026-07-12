package com.andrey.inventario.proyecto.models.category;

import java.util.HashSet;
import java.util.Set;

import com.andrey.inventario.proyecto.models.product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// JPA needs a constructor without arguments.
public class Category {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    /*
     *
     * mappedBy: Le dice a JPA "la otra clase ya configuró la relación"
     *
     * - Product tiene configurado @ManyToMany con @JoinTable (es la dueña)
     * - Category pone mappedBy.
     *
     * Si no pones mappedBy, JPA crea dos tablas intermedias.
     *
     * Da igual quien sea el dueño, lo importante es que una de las clases
     * tenga @JoinTable y el otro mappedBy.
     */

    /*
     * Sí, es una buena práctica para entidades JPA. Es más, muchos expertos
     * recomiendan no usar @Data en absoluto en entidades, precisamente por estos
     * problemas. La solución que te di (usar @Getter @Setter y definir
     * equals/hashCode basados en el ID) es estándar.
     */
}
