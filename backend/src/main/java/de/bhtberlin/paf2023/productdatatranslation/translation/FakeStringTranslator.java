package de.bhtberlin.paf2023.productdatatranslation.translation;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Fakes the translation of Strings by adding prefixes for the
 * translated language. This {@link Translator} could be used during
 * testing or development.
 *
 * @deprecated This could also be implemented as a FakeTextTranslationStrategy used
 * by the {@link StrategyTranslator}. This remains as an  example implementation
 * of a simple {@link Translator} without strategies.
 */
@Deprecated(since = "0.1.0", forRemoval = false)
public class FakeStringTranslator extends BasicTranslator {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translateText(String text, String from, String to) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return this.replaceLocalePrefix(text, to + ":");
    }

    /**
     * Replace the language specific prefix with a new prefix.
     *
     * @param text      The altered text.
     * @param newPrefix The new prefix.
     * @return The text with the new prefix.
     */
    private String replaceLocalePrefix(String text, String newPrefix) {
        String prefixPattern = "^[a-z]{2}(-[A-Z]{2})?:";
        if (Pattern.matches(prefixPattern, text)) {
            text = text.replaceFirst(prefixPattern, newPrefix);
        } else {
            text = newPrefix + text;
        }
        return text;
    }
}
