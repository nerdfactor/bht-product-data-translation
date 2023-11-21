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
 * {@link Color Colors} are a complex property for {@link Product Products}.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Color {

    /**
     * An internal identifier for the {@link Color}.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the {@link Color} normalized to german.
     */
    private String name;

    /**
     * All the {@link Product Products} that have this {@link Color}.
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "colors", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Product> products;

    /**
     * Basic constructor with all data fields in order to create new {@link Color Colors}.
     * The id will be set during creation of the object in the database.
     */
    public Color(String name) {
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
     * Compare an Object to this {@link Color} by checking
     * object equality or the same id.
     *
     * @param o The Object to compare.
     * @return true if Object is equal to this {@link Color}.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Color obj = (Color) o;
        return o == this || this.id == obj.id;
    }
}
