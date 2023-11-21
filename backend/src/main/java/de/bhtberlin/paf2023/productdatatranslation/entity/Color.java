package de.bhtberlin.paf2023.productdatatranslation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


/**
 * {@link Color Colors} are a complex property for {@link Product Products}.
 */
@Getter
@Setter
@Entity
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
}
