package de.bhtberlin.paf2023.productdatatranslation.translation.api;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.Translation;
import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * External translation Api using the Google Cloud Api.
 */
@Component
@RequiredArgsConstructor
public class GoogleCloudTranslationApi implements ExternalTranslationApi {

    /**
     * The internal Google Cloud {@link Translate}.
     */
    private final Translate translate;

    /**
     * Supported locales.
     */
    private static final Set<String> supportedLocales = Set.of("af", "sq", "am", "ar", "hy", "as", "ay", "az", "bm", "eu", "be", "bn", "bs", "bg", "ca", "zh-CN", "zh-TW", "co", "hr", "cs", "da", "dv", "nl", "en", "en-GB", "en-US", "eo", "et", "ee", "fi", "fr", "fy", "gl", "ka", "de", "el", "gn", "gu", "ht", "ha", "he", "hi", "hu", "is", "ig", "id", "ga", "it", "ja", "jv", "kn", "kk", "km", "rw", "ko", "ku", "ky", "lo", "la", "lv", "ln", "lt", "lg", "lb", "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mn", "my", "ne", "no", "ny", "or", "om", "ps", "fa", "pl", "pt", "pa", "qu", "ro", "ru", "sm", "sa", "gd", "sr", "st", "sn", "sd", "si", "sk", "sl", "so", "es", "su", "sw", "sv", "tl", "tg", "ta", "tt", "te", "th", "ti", "ts", "tr", "tk", "ak", "uk", "ur", "ug", "uz", "vi", "cy", "xh", "yi", "yo", "zu");

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Set<String> getSupportedLocales() {
        return supportedLocales;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translate(@Nullable String text, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException {
        if (text == null || text.isEmpty()) {
            return "";
        }
        try {
            Translation translation = translate.translate(
                    text,
                    TranslateOption.sourceLanguage(from),
                    TranslateOption.targetLanguage(to)
            );
            return translation.getTranslatedText();
        } catch (Exception e) {
            throw new ExternalTranslationApiException("Exception during Google Cloud Api translation", e);
        }
    }
}
