package de.bhtberlin.paf2023.productdatatranslation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;

    /**
     * Basic constructor with all data fields in order to create new {@link Category Categories}.
     * The id will be set during creation of the object in the database.
     */
    public Category(String name) {
        this.name = name;
    }
}
