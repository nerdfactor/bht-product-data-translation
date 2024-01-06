package de.bhtberlin.paf2023.productdatatranslation.translation.strategy;

import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * External translation Api using the Deepl Api.
 */
@Component
@RequiredArgsConstructor
public class DeeplTranslationStrategy implements ExternalTranslationApiStrategy {

    /**
     * The internal Deepl {@link Translator}.
     */
    private final Translator translator;

    /**
     * Supported locales.
     */
    private static final Set<String> supportedLocales = Set.of("cs", "bg", "da", "de", "el", "en", "en-GB", "en-US", "es", "et", "fi", "fr", "hu", "id", "it", "ja", "ko", "lt", "lv", "nb", "nl", "pl", "pt", "pt-BR", "pt-PT", "ro", "ru", "sk", "sl", "sv", "tr", "uk", "zh");

    /**
     * {@inheritDoc}
     */
    public @NotNull Set<String> getSupportedLocales() {
        return supportedLocales;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translateText(@Nullable String text, @NotNull String from, @NotNull String to) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (isNotSupportedLocale(from) || isNotSupportedLocale(to)) {
            return text;
        }

        // Deepl does only support en-US or en-GB.
        if (to.equalsIgnoreCase("en")) {
            to = "en-US";
        }

        try {
            TextResult result = translator.translateText(text, from, to);
            return result.getText();
        } catch (Exception e) {
            return text;
        }
    }
}
