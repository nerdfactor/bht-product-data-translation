package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.ExternalTranslationApi;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Text translation strategy that acts as a bridge to an {@link ExternalTranslationApi}
 * that will be used for translation.
 * <br>
 * The different implementations of {@link ExternalTranslationApi} could also be
 * implemented als different {@link ExternalApiTextTranslationStrategy} but separating
 * the api from the strategy may make the api also reusable in different classes
 * that are not a {@link TextTranslationStrategy}.
 */
@RequiredArgsConstructor
public class ExternalApiTextTranslationStrategy implements TextTranslationStrategy {

    final ExternalTranslationApi externalTranslationApi;

    /**
     * {@inheritDoc}
     * // todo: what to do if external translation failed or not supported? use some internal translator as fallback?
     */
    @Override
    public @NotNull String translateText(@Nullable String text, @NotNull String from, @NotNull String to) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        if (externalTranslationApi.isNotSupportedLocale(from) || externalTranslationApi.isNotSupportedLocale(to)) {
            return text;
        }
        try {
            return externalTranslationApi.translate(text, from, to);
        } catch (ExternalTranslationApiException e) {
            return text;
        }
    }
}
