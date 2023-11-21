package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Data transfer object to transfer all necessary data that is
 * used during the "AddNewProductIntoSystem" user story.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AddNewProductDto {

    private String serial;

    private String name;

    private double height;

    private double width;

    private double depth;

    private double weight;

    private double price;

    private LanguageDto language;

    private String shortDescription;

    private String longDescription;

    /**
     * Existing categories can be transferred as objects.
     */
    private Set<CategoryDto> existingCategories = new HashSet<>();

    /**
     * New categories have to be transferred separately in order to
     * create them before they are added to the product.
     * todo: change from strings to DTOs?
     */
    private Set<String> addedCategories = new HashSet<>();

    private Set<ColorDto> colors = new HashSet<>();
}
