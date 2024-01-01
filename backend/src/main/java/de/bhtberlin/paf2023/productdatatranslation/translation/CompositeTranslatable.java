package de.bhtberlin.paf2023.productdatatranslation.translation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Composite of multiple {@link Translatable Translatables} that can be
 * translated by a {@link Translator} as if it was one {@link Translatable}.
 */
public interface CompositeTranslatable extends Translatable {

    /**
     * Add a {@link Translatable} to the composite.
     *
     * @param translatable The {@link Translatable} to add.
     */
    void addTranslatable(@NotNull Translatable translatable);

    /**
     * Remove a {@link Translatable} from the composite.
     *
     * @param translatable The {@link Translatable} to remove.
     */
    void removeTranslatable(@NotNull Translatable translatable);

    /**
     * Get a list of all {@link Translatable Translatables} in the composite.
     *
     * @return A list of {@link Translatable Translatables} in the composite.
     */
    List<Translatable> getTranslatables();
}
