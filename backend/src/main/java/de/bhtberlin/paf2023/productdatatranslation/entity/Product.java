package de.bhtberlin.paf2023.productdatatranslation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A Product is a producible thing in the real world defined
 * by its properties and descriptions. Products can be sorted
 * into multiple categories. Every product will be uniquely
 * identified by its serial number.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	/**
	 * A serial number identifying the product.
	 */
	@Id
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

}
