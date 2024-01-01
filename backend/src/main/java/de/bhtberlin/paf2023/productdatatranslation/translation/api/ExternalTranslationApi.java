package de.bhtberlin.paf2023.productdatatranslation.translation.api;

import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * Facade for external translations Apis.
 */
@Component
public interface ExternalTranslationApi {

    /**
     * Will translate a text from a specified language to a different specified language.
     *
     * @param text The original string.
     * @param from   The original language.
     * @param to     The target language.
     * @return The translated string.
     * @throws ExternalTranslationApiException If something did not work during translation.
     */
    @NotNull String translate(@NotNull String text, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException;
}
