package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * A Product is a type of producible thing in the real world
 * defined by its properties and descriptions. Products can be
 * sorted into multiple categories. Every product will be
 * uniquely identified by an internal identifier.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product {

    /**
     * An internal identifier for the {@link Product}.
     * The id will be automatically incremented by the database.
     * todo: Decide if this and other IDs should be changed to UUID that can be set in frontend instead in the database?
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * A serial number for the {@link Product}.
     * This is not the internal identifier.
     * It should be unique but can't be relied on to be globally unique
     * with different producers. A globally unique serial number could
     * be something like a ISBN for books.
     */
    private String serial;

    /**
     * A name for the {@link Product} normalized to german.
     */
    private String name;

    /**
     * The height of the {@link Product}.
     */
    private double height;

    /**
     * The width of the {@link Product}.
     */
    private double width;

    /**
     * The depth of the {@link Product}.
     */
    private double depth;

    /**
     * The weight of the {@link Product}.
     */
    private double weight;

    /**
     * The price of the {@link Product}.
     */
    private double price;

    /**
     * The {@link Category Categories} for this {@link Product}.
     */
    @ManyToMany(mappedBy = "products")
    private Set<Category> categories;

    /**
     * The {@link Color Colors} for this {@link Product}.
     */
    @ManyToMany(mappedBy = "products")
    private Set<Color> colors;

    /**
     * The {@link Picture Pictures} for this {@link Product}.
     */
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Picture> pictures;

    /**
     * The {@link Translation ranslations} for this {@link Product}.
     */
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Translation> translations;

    /**
     * Basic constructor with all data fields in order to create new {@link Product Products}.
     * The id will be set during creation of the object in the database.
     */
    public Product(String serial, String name, double height, double width, double depth, double weight, double price) {
        this.serial = serial;
        this.name = name;
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.weight = weight;
        this.price = price;
    }
}
