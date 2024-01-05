package de.bhtberlin.paf2023.productdatatranslation.translation;

import com.fasterxml.jackson.databind.json.JsonMapper;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.ExternalTranslationApi;
import de.bhtberlin.paf2023.productdatatranslation.translation.api.GoogleWebTranslationApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GoogleWebTranslationApiTest {

    ExternalTranslationApi api = new GoogleWebTranslationApi(new JsonMapper());

    /**
     * Check if simple string can be translated from English to German.
     */
    @Test
    void shouldTranslateBasicText() {
        String expected = "Dies ist ein Beispiel für einen übersetzbaren Text.";
        String result = this.api.translate("This is a sample translatable text.", "en", "de");
        Assertions.assertEquals(expected, result);
    }

    /**
     * Check if string with umlauts can be translated between German, French and
     * English and back to German.
     */
    @Test
    void shouldTranslateTextWithUmlaut() {
        String textGerman = "Ein böser Text mit fürchterlich schönen Umlauten.";
        String expectedFrench = "Un texte méchant avec des trémas terriblement beaux.";
        String expectedEnglish = "A nasty text with terribly beautiful umlauts.";
        String expectedGerman = "Ein fieser Text mit furchtbar schönen Umlauten.";
        String resultFrench = this.api.translate(textGerman, "de", "fr");
        Assertions.assertEquals(expectedFrench, resultFrench);
        String resultEnglish = this.api.translate(expectedFrench, "fr", "en");
        Assertions.assertEquals(expectedEnglish, resultEnglish);
        String resultGerman = this.api.translate(expectedEnglish, "en", "de");
        Assertions.assertEquals(expectedGerman, resultGerman);
    }

}
