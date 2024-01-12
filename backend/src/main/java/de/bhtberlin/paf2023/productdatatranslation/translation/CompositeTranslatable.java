package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
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

    /**
     * Translate the composite and all containing {@link Translatable Translatables}.
     * <br>
     * Instead of directly getting a {@link Translator} for the translation it will
     * get a {@link TranslationVisitor} and delegates the translation back to the
     * Visitor.
     * <br>
     * See the doc of {@link Translatable#translate(TranslationVisitor, Language, Language)} for
     * explanation of the default implementation.
     *
     * @param visitor The visiting TranslationVisitor.
     * @param from    The {@link Language} of the current locale.
     * @param to      The {@link Language} of the target locale.
     * @return The translated {@link Translatable}.
     */
    @Override
    default @NotNull Translatable translate(@NotNull TranslationVisitor visitor, @NotNull Language from, @NotNull Language to) {
        return visitor.deferredVisit(this, from, to);
    }
}
