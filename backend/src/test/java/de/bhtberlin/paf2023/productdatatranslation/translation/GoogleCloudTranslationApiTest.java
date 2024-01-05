package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.api.GoogleCloudTranslationApi;
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
