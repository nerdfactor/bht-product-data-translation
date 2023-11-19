package de.bhtberlin.paf2023.productdatatranslation.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * A {@link Language} that can be used to translate the application ui
 * and {@link Product Products}.
 */
@Getter
@Setter
@Entity
public class Language {

    /**
     * An internal identifier for the {@link Language}.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * A name for this {@link Language} in german.
     */
    private String name;

    /**
     * The default currency for this {@link Language}.
     * todo: May be changed into entity if more complex features are needed.
     */
    private String currency;

    /**
     * The default system of measurement for this
     * {@link Language}..
     * todo: May be changed into enum if systems should be predefined.
     * todo: May be changed into entity if more complex features are needed.
     */
    private String measure;

    /**
     * The iso code for this {@link Language}.
     * todo: May be changed into enum if isoCodes should be predefined.
     */
    private String isoCode;

    /**
     * All {@link Translation Translations} that are in this {@link Language}.
     */
    @OneToMany(mappedBy = "language", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Translation> translations;
}
