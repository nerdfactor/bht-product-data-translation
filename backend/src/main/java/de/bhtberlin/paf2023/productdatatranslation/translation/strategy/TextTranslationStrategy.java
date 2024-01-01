package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import org.jetbrains.annotations.NotNull;

/**
 * Strategy for text translation.
 */
public interface TextTranslationStrategy {

    /**
     * Translate a text.
     *
     * @param text The text to translate.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return The translated text.
     */
    default @NotNull String translateText(@NotNull String text, @NotNull String from, @NotNull String to) {
        return text;
    }
}
