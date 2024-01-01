package de.bhtberlin.paf2023.productdatatranslation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanguageServiceTest {

    /**
     * Check if language tags can be normalized to the same format.
     */
    @Test
    void shouldNormalizeLanguageTags() {
        Assertions.assertEquals("de", LanguageService.normalizeLanguageTag("de"));
        Assertions.assertEquals("de", LanguageService.normalizeLanguageTag("de-DE"));
        Assertions.assertEquals("de", LanguageService.normalizeLanguageTag("de_DE"));
        Assertions.assertEquals("en-US", LanguageService.normalizeLanguageTag("en_US"));
    }
}
