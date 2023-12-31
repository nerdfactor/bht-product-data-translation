package de.bhtberlin.paf2023.productdatatranslation.service.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.Translation;
import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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
	 * {@inheritDoc}
	 */
	@Override
	public @NotNull String translate(@NotNull String string, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException {
		try {
			Translation translation = translate.translate(
					string,
					TranslateOption.sourceLanguage(from),
					TranslateOption.targetLanguage(to)
			);
			return translation.getTranslatedText();
		} catch (Exception e) {
			throw new ExternalTranslationApiException("Exception during Google Cloud Api translation", e);
		}
	}
}
