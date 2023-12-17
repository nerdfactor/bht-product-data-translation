package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Description {

    /**
     * An internal identifier for the {@link Translation}.
     * The id will be automatically incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * A short description for the {@link Product}.
     */
    protected String shortDescription;

    /**
     * A longer description for the {@link Product}.
     */
    @Lob
    protected String longDescription;

    /**
     * The {@link Product} this {@link Translation} is for.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    protected Product product;

    /**
     * Compare an Object to this {@link Translation} by checking
     * object equality or the same id.
     *
     * @param o The Object to compare.
     * @return true if Object is equal to this {@link Translation}.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Translation obj = (Translation) o;
        return o == this || this.id == obj.id;
    }
}
