package de.bhtberlin.paf2023.productdatatranslation.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Language} that can be used to translate the application ui
 * and {@link Product Products}.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
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

    /**
     * Basic constructor with all data fields in order to create new {@link Language Languages}.
     * The id will be set during creation of the object in the database.
     */
    public Language(String name, String currency, String measure, String isoCode) {
        this.name = name;
        this.currency = currency;
        this.measure = measure;
        this.isoCode = isoCode;
    }

    /**
     * Add a {@link Translation}.
     *
     * @param translation The {@link Translation} to add.
     */
    public void addTranslation(@NotNull Translation translation) {
        if (this.translations == null) {
            this.translations = new HashSet<>();
        }
        this.translations.add(translation);
    }

    /**
     * Remove a {@link Translation}.
     *
     * @param translation The {@link Translation} to remove.
     */
    public void removeTranslation(@NotNull Translation translation) {
        if (this.translations == null) {
            this.translations = new HashSet<>();
        }
        this.translations.remove(translation);
    }

    /**
     * Compare an Object to this {@link Language} by checking
     * object equality or the same id.
     *
     * @param o The Object to compare.
     * @return true if Object is equal to this {@link Language}.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Language obj = (Language) o;
        return o == this || this.id == obj.id;
    }

}
