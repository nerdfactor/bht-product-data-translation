package de.bhtberlin.paf2023.productdatatranslation.translation.api;

import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * External translation Api using the Deepl Api.
 */
@Component
@RequiredArgsConstructor
public class DeeplTranslationApi implements ExternalTranslationApi {

    /**
     * The internal Deepl {@link Translator}.
     */
    private final Translator translator;

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translate(@NotNull String text, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException {
        try {
            TextResult result = translator.translateText(text, from, to);
            return result.getText();
        } catch (Exception e) {
            throw new ExternalTranslationApiException("Exception during Deepl Api translation", e);
        }
    }
}
