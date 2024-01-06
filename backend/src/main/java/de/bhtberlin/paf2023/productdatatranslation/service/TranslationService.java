package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import de.bhtberlin.paf2023.productdatatranslation.repo.TranslationRepository;
import de.bhtberlin.paf2023.productdatatranslation.translation.AutoTranslationCache;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.TranslationVisitor;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service acting as a facade for translation related tasks.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    /**
     * An implementation of {@link Translator} that takes care of translation
     * and conversion of text, currencies and measurements.
     */
    final Translator translator;

    final AutoTranslationCache translationCache;

    /**
     * An implementation of a {@link TranslationRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final TranslationRepository translationRepository;

    /**
     * An implementation of a {@link LanguageRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final LanguageRepository languageRepository;

    /**
     * Translate a {@link Product} into a specific language.
     * <br>
     * This will take the default {@link Translation}, translate it to the
     * desired language and add it to the list of translations for the {@link Product}.
     * <br>
     * If the {@link Translation} for this language already exists, it will be
     * updated.
     *
     * @param product The {@link Product} to translate.
     * @param to      The tag of the target locale.
     * @return The {@link Product} with the new/updated {@link Translation}.
     * @throws TranslationException If there was a problem during translation.
     */
    public Product translateProduct(Product product, String to) throws TranslationException {
        if (to.equalsIgnoreCase(AppConfig.DEFAULT_LANGUAGE)) {
            throw new TranslationException("Can not translate into default language.");
        }
        Language defaultLanguage = this.languageRepository.findOneByIsoCode(AppConfig.DEFAULT_LANGUAGE)
                .orElseThrow(() -> new TranslationException("Could not find default Language."));
        to = LanguageService.normalizeLanguageTag(to);
        Language language = this.languageRepository.findOneByIsoCode(to)
                .orElseThrow(() -> new TranslationException("Could not find Language for translation."));

        // get default translation from the product
        Translation defaultTranslation = this.translationRepository.getOneByProductAndLanguage(product, defaultLanguage);

        Translation translation = this.translationRepository.getOneByProductAndLanguage(product, language);

        if (translation == null) {
            // create new translation
            translation = new Translation();
            translation.setLanguage(language);
            product.addTranslation(translation);
            translation.setProduct(product);
        }

        // set the descriptions to the descriptions of the default translation.
        translation.setShortDescription(defaultTranslation.getShortDescription());
        translation.setLongDescription(defaultTranslation.getLongDescription());

        // let the translator translate it
        translation.setShortDescription(this.translationCache.cachedTranslate(
                translation.getShortDescription(),
                defaultTranslation.getLanguage().getIsoCode(),
                to,
                translator
        ));
        translation.setLongDescription(this.translationCache.cachedTranslate(
                translation.getLongDescription(),
                defaultTranslation.getLanguage().getIsoCode(),
                to, translator
        ));

        // save the translation
        this.translationRepository.save(translation);
        return product;
    }

    /**
     * Translate a {@link Translatable} into a specific language.
     *
     * @param translatable The {@link Translatable} to translate.
     * @param to           The tag of the target locale.
     * @return The translated {@link Translatable}.
     */
    public Translatable translateTranslatable(Translatable translatable, String from, String to) {
        return translatable.translate((TranslationVisitor) translator, from, to);
    }

    /**
     * Translate a {@link String} into a specific language.
     *
     * @param string The {@link String} to translate.
     * @param to     The tag of the target locale.
     * @return The translated {@link String}.
     */
    public String translateString(String string, String from, String to) {
        return this.translationCache.cachedTranslate(string, from, to, translator);
    }
}
