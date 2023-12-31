package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.service.translation.GoogleCloudTranslationApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GoogleCloudTranslationApiTest {

	@Autowired
	GoogleCloudTranslationApi api;

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
