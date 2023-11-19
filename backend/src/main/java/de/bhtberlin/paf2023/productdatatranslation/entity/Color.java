package de.bhtberlin.paf2023.productdatatranslation.entity;

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
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "color_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;
}
