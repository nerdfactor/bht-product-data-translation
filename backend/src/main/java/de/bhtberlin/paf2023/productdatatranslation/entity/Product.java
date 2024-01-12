package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
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
     * todo: Decide if this and other IDs should be changed to UUID that can be set in frontend instead of in the database?
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    /**
     * The {@link Color Colors} for this {@link Product}.
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private Set<Color> colors = new HashSet<>();

    /**
     * The {@link Picture Pictures} for this {@link Product}.
     */
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Picture> pictures = new HashSet<>();

    /**
     * The {@link Translation ranslations} for this {@link Product}.
     */
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Set<Translation> translations = new HashSet<>();

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

    /**
     * Add a {@link Category}.
     *
     * @param category The {@link Category} to add.
     */
    public void addCategory(@NotNull Category category) {
        if (this.categories == null) {
            this.categories = new HashSet<>();
        }
        this.categories.add(category);
    }

    /**
     * Remove a {@link Category}.
     *
     * @param category The {@link Category} to remove.
     */
    public void removeCategory(@NotNull Category category) {
        if (this.categories == null) {
            this.categories = new HashSet<>();
        }
        this.categories.remove(category);
    }

    /**
     * Add a {@link Color}.
     *
     * @param color The {@link Color} to add.
     */
    public void addColor(@NotNull Color color) {
        if (this.colors == null) {
            this.colors = new HashSet<>();
        }
        this.colors.add(color);
    }

    /**
     * Remove a {@link Color}.
     *
     * @param color The {@link Color} to remove.
     */
    public void removeColor(@NotNull Color color) {
        if (this.colors == null) {
            this.colors = new HashSet<>();
        }
        this.colors.remove(color);
    }

    /**
     * Add a {@link Picture}.
     *
     * @param picture The {@link Picture} to add.
     */
    public void addPicture(@NotNull Picture picture) {
        if (this.pictures == null) {
            this.pictures = new HashSet<>();
        }
        this.pictures.add(picture);
    }

    /**
     * Remove a {@link Picture}.
     *
     * @param picture The {@link Picture} to remove.
     */
    public void removePicture(@NotNull Picture picture) {
        if (this.pictures == null) {
            this.pictures = new HashSet<>();
        }
        this.pictures.remove(picture);
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

    public void removeTranslationsNotInLocale(@NotNull String locale) {
        this.translations.removeIf(translation -> !translation.getLanguage().getIsoCode().equalsIgnoreCase(locale));
    }

    public boolean hasTranslations() {
        return !this.translations.isEmpty();
    }

    /**
     * Compare an Object to this {@link Product} by checking
     * object equality or the same id.
     *
     * @param o The Object to compare.
     * @return true if Object is equal to this {@link Product}.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product obj = (Product) o;
        return o == this || this.id == obj.id;
    }

}
