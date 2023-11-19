package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * A {@link Translation} contains the description for a {@link Product}
 * in a specific language.
 */
@Getter
@Setter
@Entity
public class Translation {

    /**
     * An internal identifier for the {@link Translation}.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * A short description for the {@link Product}.
     */
    private String shortDescription;

    /**
     * A longer description for the {@link Product}.
     */
    @Lob
    private String longDescription;

    /**
     * The {@link Product} this {@link Translation} is for.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Multiple {@link Revision Revisions} for this {@link Translation}.
     */
    @OneToMany(mappedBy = "translation", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Revision> revisions;

    /**
     * The {@link Language} for this {@link Translation}.
     */
    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;
}
