package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Translation} contains the description for a {@link Product}
 * in a specific language.
 * todo: rename to Description in order not to confuse translated Data in {@link Translation} with actual translation functionality.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
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
    @Column(length = 3000)
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

	/**
	 * Basic constructor with all data fields in order to create new {@link Translation Translations}.
	 * The id will be set during creation of the object in the database.
	 */
	public Translation(String shortDescription, String longDescription) {
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	public Translation(String shortDescription, String longDescription, Language language) {
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.language = language;
	}

	/**
	 * Add a {@link Revision}.
	 *
	 * @param revision The {@link Revision} to add.
	 */
	public void addRevision(@NotNull Revision revision) {
		if (this.revisions == null) {
			this.revisions = new HashSet<>();
		}
		this.revisions.add(revision);
	}

	/**
	 * Remove a {@link Revision}.
	 *
	 * @param revision The {@link Revision} to remove.
	 */
	public void removeRevision(@NotNull Revision revision) {
		if (this.revisions == null) {
			this.revisions = new HashSet<>();
		}
		this.revisions.remove(revision);
	}

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
