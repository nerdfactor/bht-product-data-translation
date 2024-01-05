package de.bhtberlin.paf2023.productdatatranslation.translation;

import org.jetbrains.annotations.NotNull;


/**
 * An object that can be translated by a {@link Translator}.
 */
public interface Translatable {

    /**
     * Translate the object with the help of a {@link Translator}.
     * <br>
     *
     * @param translator The used {@link Translator}.
     * @param from       The tag of the current locale.
     * @param to         The tag of the target locale.
     * @return The translated {@link Translatable}.
     * @deprecated Use {@link #translate(TranslationVisitor, String, String)} instead
     * to favor the implementation of the translation inside the visitor. The default
     * implementation helps by casting the {@link Translator} into a {@link TranslationVisitor}.
     */
    @Deprecated
    default @NotNull Translatable translate(@NotNull Translator translator, @NotNull String from, @NotNull String to) {
        return this.translate((TranslationVisitor) translator, from, to);
    }

    /**
     * Translate the object.
     * <br>
     * Instead of directly getting a {@link Translator} for the translation it will
     * get a {@link TranslationVisitor} and delegates the translation back to the
     * Visitor.
     * <br>
     * The default implementation uses {@link TranslationVisitor#deferredVisit(Translatable, String, String)}
     * to defer the decision which method should be used for the concrete
     * {@link Translatable} to the {@link TranslationVisitor}. This would centralize
     * the implementations even more into the {@link TranslationVisitor} and
     * not split it over all {@link Translatable}.
     *
     * @param visitor The visiting TranslationVisitor.
     * @param from    The tag of the current locale.
     * @param to      The tag of the target locale.
     * @return The translated {@link Translatable}.
     */
    default @NotNull Translatable translate(@NotNull TranslationVisitor visitor, @NotNull String from, @NotNull String to) {
        return visitor.deferredVisit(this, from, to);
    }
}
