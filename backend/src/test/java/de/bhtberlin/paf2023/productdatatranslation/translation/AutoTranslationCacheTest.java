package de.bhtberlin.paf2023.productdatatranslation.translation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

@SpringBootTest
class AutoTranslationCacheTest {

    @Autowired
    AutoTranslationCache autoTranslationCache;

    @Autowired
    Translator translator;

    /**
     * Check if a translation is cached.
     */
    @Test
    void shouldHitCacheDuringTranslation() {
        String text = "Ein mehrfach wiederholbarer Text";
        this.translator.translateText(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag());
        Assertions.assertTrue(this.autoTranslationCache.textIsCached(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag()));
    }

    /**
     * Check if the same text is only cached once.
     */
    @Test
    void shouldAddToCacheJustOnce() {
        String text = "Ein mehrfach wiederholbarer Text";
        this.translator.translateText(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag());
        this.translator.translateText(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag());
        this.translator.translateText(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag());
        this.translator.translateText(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag());
        Assertions.assertTrue(this.autoTranslationCache.textIsCached(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag()));
        Assertions.assertEquals(1, this.autoTranslationCache.getCacheSize());
    }

    /**
     * Check if the same text is cached separately for each target language.
     */
    @Test
    void shouldCacheInDifferentLanguages() {
        String text = "Ein mehrfach wiederholbarer Text";
        this.translator.translateText(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag());
        this.translator.translateText(text, Locale.GERMAN.toLanguageTag(), Locale.FRENCH.toLanguageTag());
        Assertions.assertTrue(this.autoTranslationCache.textIsCached(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag()));
        Assertions.assertEquals(2, this.autoTranslationCache.getCacheSize());
    }

    /**
     * Check if the wrapping of a {@link Translator} with the cache is cached.
     */
    @Test
    void shouldCacheWrappedTranslation() {
        String text = "Ein mehrfach wiederholbarer Text";
        this.autoTranslationCache.cachedTranslate(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag(), this.translator);
        Assertions.assertTrue(this.autoTranslationCache.textIsCached(text, Locale.GERMAN.toLanguageTag(), Locale.ENGLISH.toLanguageTag()));
        Assertions.assertEquals(1, this.autoTranslationCache.getCacheSize());
    }
}
