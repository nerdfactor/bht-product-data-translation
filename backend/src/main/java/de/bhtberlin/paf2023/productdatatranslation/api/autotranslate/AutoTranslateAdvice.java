package de.bhtberlin.paf2023.productdatatranslation.api.autotranslate;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageService;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.TranslationVisitor;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * An abstract AutoTranslateAdvice providing the common functionality for
 * translating {@link Translatable} objects.
 *
 * @param <T> The type of the {@link Translatable} to translate.
 */
public abstract class AutoTranslateAdvice<T> implements ResponseBodyAdvice<T> {

    /**
     * An implementation of {@link Translator} that takes care of translation
     * and conversion of text, currencies and measurements.
     */
    @Autowired
    protected Translator translator;

    /**
     * The {@link LanguageService} for access to {@link Language Languages}.
     */
    @Autowired
    protected LanguageService languageService;

    /**
     * Translate a {@link Translatable} into a specific language.
     *
     * @param translatable The {@link Translatable} to translate.
     * @param to           The tag of the target locale.
     * @return The translated {@link Translatable}.
     */
    protected Translatable translateTranslatable(Translatable translatable, String from, String to) {
        Language fromLang = this.languageService.getByIsoCode(from);
        Language toLang = this.languageService.getByIsoCode(to);
        return translatable.translate((TranslationVisitor) translator, fromLang, toLang);
    }
}
