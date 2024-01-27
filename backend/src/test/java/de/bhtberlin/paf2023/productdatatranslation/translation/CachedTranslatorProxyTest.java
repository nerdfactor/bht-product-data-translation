package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.translation.caching.AutoTranslationCache;
import de.bhtberlin.paf2023.productdatatranslation.translation.caching.CachedTranslatorProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

@SpringBootTest
class CachedTranslatorProxyTest {

    @Autowired
    AutoTranslationCache autoTranslationCache;

    @Autowired
    Translator translator;

    /**
     * Check if the {@link Translator} is wrapped in a {@link CachedTranslatorProxy}
     * when the translation cache is configured.
     * The default application.yaml used for the tests should have the cache enabled.
     */
    @Test
    void shouldCreateCachedTranslatorProxy() {
        Assertions.assertInstanceOf(CachedTranslatorProxy.class, this.translator);
    }

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
}
