package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import de.bhtberlin.paf2023.productdatatranslation.service.LanguageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * External Api that translates texts.
 */
@Component
public interface ExternalTranslationApiStrategy extends TextTranslationStrategy {

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
}