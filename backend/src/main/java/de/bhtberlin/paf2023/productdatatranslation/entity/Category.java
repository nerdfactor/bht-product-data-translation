package de.bhtberlin.paf2023.productdatatranslation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


/**
 * A {@link Category} groups {@link Product Products} by their type.
 * Categories can be something like smartphone, notebook or car
 * but also much more generic or detailed.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Category {

    /**
     * An internal identifier for the {@link Category}.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the {@link Category} normalized to german.
     */
    private String name;

    /**
     * All the {@link Product Products} that are in this {@link Category}.
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "categories", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Product> products;

    /**
     * Basic constructor with all data fields in order to create new {@link Category Categories}.
     * The id will be set during creation of the object in the database.
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Add a {@link Product}.
     *
     * @param product The {@link Product} to add.
     */
    public void addProduct(@NotNull Product product) {
        if (this.products == null) {
            this.products = new HashSet<>();
        }
        this.products.add(product);
    }

    /**
     * Remove a {@link Product}.
     *
     * @param product The {@link Product} to remove.
     */
    public void removeProduct(@NotNull Product product) {
        if (this.products == null) {
            this.products = new HashSet<>();
        }
        this.products.remove(product);
    }

    /**
     * Compare an Object to this {@link Category} by checking
     * object equality or the same id.
     *
     * @param o The Object to compare.
     * @return true if Object is equal to this {@link Category}.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category obj = (Category) o;
        return o == this || this.id == obj.id;
    }
}
