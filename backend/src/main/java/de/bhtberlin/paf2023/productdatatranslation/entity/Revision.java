package de.bhtberlin.paf2023.productdatatranslation.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link Revision} is the historical version of a {@link Translation}.
 */
@Getter
@Setter
@Entity
public class Revision {

    /**
     * An internal identifier for the {@link Revision}.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The version number for this {@link Revision} that will be
     * counted up for every changed {@link Translation}.
     */
    private int version;

    /**
     * A timestamp when the {@link Revision} was created.
     */
    private long timestamp;

    /**
     * The short description from the original {@link Translation}.
     */
    private String shortDescription;

    /**
     * The longer description from the original {@link Translation}.
     */
    private String longDescription;

    /**
     * Information if this {@link Revision} is a correction.
     */
    private boolean correction;

    /**
     * The {@link Translation} this {@link Revision} is for.
     */
    @ManyToOne
    @JoinColumn(name = "translation_id")
    private Translation translation;
}
