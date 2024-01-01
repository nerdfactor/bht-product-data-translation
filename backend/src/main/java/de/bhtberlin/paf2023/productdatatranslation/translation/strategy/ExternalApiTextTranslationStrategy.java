package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.ExternalTranslationApi;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Text translation strategy that will use an external api for translation.
 */
@RequiredArgsConstructor
public class ExternalApiTextTranslationStrategy implements TextTranslationStrategy {

    final ExternalTranslationApi externalTranslationApi;

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translateText(@NotNull String text, @NotNull String from, @NotNull String to) {
        try {
            return externalTranslationApi.translate(text, from, to);
        } catch (ExternalTranslationApiException e) {
            // todo: what to do if external translation failed? use some internal translator as fallback?
            return text;
        }
    }
}
