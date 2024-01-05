package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;

/**
 * Visits {@link Translatable} objects in order to delegate the
 * translation method to the Visitor.
 * <p>
 * Can be used on concrete Implementations of {@link Translatable} by
 * them using the matching visit() method or by using the deferredVisit()
 * method in order to defer the choice of a concrete implementation
 * to the visitor (which would break the visitor pattern, but makes the implementation
 * much easier).
 */
public interface TranslationVisitor {

    /**
     * Defer the decision about the concrete implementation of {@link Translatable}
     * back to the visitor. This allows to centralize the translation logic
     * even more. The concrete {@link Translatable} don't need to implement
     * their own logic how to delegate back to the visitor.
     *
     * @param translatable The visited {@link Translatable}.
     * @param from         The tag of the current locale.
     * @param to           The tag of the target locale.
     * @return The translated object.
     */
    Translatable deferredVisit(Translatable translatable, String from, String to);

    /**
     * Translate a {@link ProductDto}.
     *
     * @param dto  The visited {@link ProductDto}.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return The translated object.
     */
    ProductDto visit(ProductDto dto, String from, String to);

    /**
     * Translate a {@link ColorDto}.
     *
     * @param dto  The visited {@link ColorDto}.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return The translated object.
     */
    ColorDto visit(ColorDto dto, String from, String to);

    /**
     * Translate a {@link CategoryDto}.
     *
     * @param dto  The visited {@link CategoryDto}.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return The translated object.
     */
    CategoryDto visit(CategoryDto dto, String from, String to);
}
