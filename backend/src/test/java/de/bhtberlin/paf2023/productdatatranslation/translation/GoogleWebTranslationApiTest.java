package de.bhtberlin.paf2023.productdatatranslation.translation;

import com.fasterxml.jackson.databind.json.JsonMapper;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.ExternalTranslationApi;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.GoogleWebTranslationApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GoogleWebTranslationApiTest {

	ExternalTranslationApi api = new GoogleWebTranslationApi(new JsonMapper());

	/**
	 * Check if simple string can be translated from english to german.
	 */
	@Test
	void shouldTranslateBasicText() {
		String expected = "Dies ist ein Beispiel für einen übersetzbaren Text.";
		String result = this.api.translate("This is a sample translatable text.", "EN", "DE");
		Assertions.assertEquals(expected, result);
	}

}
