package de.bhtberlin.paf2023.productdatatranslation.translation;

import org.jetbrains.annotations.NotNull;


/**
 * Can be translated by a {@link Translator} by delegating the translation
 * to a {@link TranslationVisitor}.
 */
public interface Translatable {

    /**
     * Translate with the help of a {@link TranslationVisitor} by delegating
     * the Operation back to the Visitor. The default implementation defers
     * the decision of the concrete method  also back to the visitor for ease
     * of use.
     *
     * @param translator The visiting Translator.
     * @param from       The tag of the current locale.
     * @param to         The tag of the target locale.
     * @return The translated object.
     */
    default @NotNull Translatable translate(@NotNull TranslationVisitor translator, @NotNull String from, @NotNull String to) {
        return translator.deferredVisit(this, from, to);
    }
}
