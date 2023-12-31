package de.bhtberlin.paf2023.productdatatranslation.service.translation;

import com.deepl.api.TextResult;
import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import com.deepl.api.Translator;

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
	public @NotNull String translate(@NotNull String string, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException {
		try {
			TextResult result = translator.translateText(string, from, to);
			return result.getText();
		} catch (Exception e) {
			throw new ExternalTranslationApiException("Exception during Deepl Api translation", e);
		}
	}
}
