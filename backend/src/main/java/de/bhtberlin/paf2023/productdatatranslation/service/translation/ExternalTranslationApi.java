package de.bhtberlin.paf2023.productdatatranslation.service.translation;

import de.bhtberlin.paf2023.productdatatranslation.exception.ExternalTranslationApiException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * Facade for external translations Apis.
 * todo: why separate ExternalTranslationApi and Translator?
 */
@Component
public interface ExternalTranslationApi {

	/**
	 * Will translate a string from a specified language to a different specified language.
	 *
	 * @param string The original string.
	 * @param from   The original language.
	 * @param to     The target language.
	 * @return The translated string.
	 * @throws ExternalTranslationApiException If something did not work during translation.
	 */
	@NotNull String translate(@NotNull String string, @NotNull String from, @NotNull String to) throws ExternalTranslationApiException;
}
