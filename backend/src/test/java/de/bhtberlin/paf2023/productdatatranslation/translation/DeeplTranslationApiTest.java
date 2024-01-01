package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.api.DeeplTranslationApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeeplTranslationApiTest {

    @Autowired
    DeeplTranslationApi api;

    /**
     * Check if simple string can be translated from english to german.
     */
    @Test
    void shouldTranslateBasicText() {
        String expected = "Dies ist ein übersetzbarer Beispieltext.";
        String result = this.api.translate("This is a sample translatable text.", "en", "de");
        Assertions.assertEquals(expected, result);
    }

}
