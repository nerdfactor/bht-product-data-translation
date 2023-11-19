package de.bhtberlin.paf2023.productdatatranslation.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link Picture Pictures} visually describes a {@link Product}.
 */
@Getter
@Setter
@Entity
public class Picture {

    /**
     * An internal identifier for the {@link Picture}.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The filename that the {@link Picture} uses. This will be the
     * normalized original filename from the upload.
     */
    private String filename;

    /**
     * The file format of the {@link Picture}. This will be set to the
     * original file format from the upload.
     * todo: May be changed into enum if formats should be predefined.
     */
    private String format;

    /**
     * The original height of the {@link Picture}.
     */
    private double height;

    /**
     * The original width of the {@link Picture}.
     */
    private double width;

    /**
     * The {@link Product} this {@link Picture} belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
