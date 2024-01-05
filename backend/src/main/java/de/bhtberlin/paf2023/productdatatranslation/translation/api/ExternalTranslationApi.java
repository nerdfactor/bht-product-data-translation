package de.bhtberlin.paf2023.productdatatranslation.translation.api;

import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * External Api that translates texts.
 */
@Component
public interface ExternalTranslationApi {

    /**
     * Get a {@link Set} of language tags of the supported locales.
     *
     * @return A {@link Set} of supported locales.
     */
    @NotNull Set<String> getSupportedLocales();

    /**
     * Check if a local is supported.
     *
     * @param locale The language tag for the locale.
     * @return If the locale is supported.
     */
    default boolean isSupportedLocale(@NotNull String locale) {
        return this.getSupportedLocales().contains(LanguageService.normalizeLanguageTag(locale));
    }

    /**
     * Check if a local not is supported.
     *
     * @param locale The language tag for the locale.
     * @return If the locale is not supported.
     */
    default boolean isNotSupportedLocale(@NotNull String locale) {
        return !this.isSupportedLocale(locale);
    }

    /**
     * Will translate a text from a specified language to a different specified language.
     *
     * @param text The original string.
     * @param from The original language.
     * @param to   The target language.
     * @return The translated string.
     * @throws ExternalTranslationApiException If something did not work during translation.
     */
    @NotNull String translate(@Nullable String text, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException;
}
