package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
     * An internal identifier for the product.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * A serial number for the product.
     * This is not the internal identifier.
     * It should be unique but can't be relied on to be globally unique
     * with different producers. A globally unique serial number could
     * be something like a ISBN for books.
     */
    private String serial;

    /**
     * A name for the product.
     */
    private String name;

    /**
     * The height of the product.
     */
    private double height;

    /**
     * The width of the product.
     */
    private double width;

    /**
     * The depth of the product.
     */
    private double depth;

    /**
     * The weight of the product.
     */
    private double weight;

    /**
     * The price of the product.
     */
    private double price;

    /**
     * Basic constructor with all data fields in order to create new products.
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
