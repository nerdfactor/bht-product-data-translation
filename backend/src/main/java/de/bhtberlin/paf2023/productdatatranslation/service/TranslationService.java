package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import de.bhtberlin.paf2023.productdatatranslation.repo.TranslationRepository;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for translation related tasks.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    final Translator translator;

    final TranslationRepository translationRepository;

    final LanguageRepository languageRepository;

    public Product translateProduct(Product product, String to) throws TranslationException {
        Language defaultLanguage = this.languageRepository.findOneByIsoCode(AppConfig.DEFAULT_LANGUAGE)
                .orElseThrow(() -> new TranslationException("Could not find default Language."));
        to = LanguageService.normalizeLanguageTag(to);
        Language language = this.languageRepository.findOneByIsoCode(to)
                .orElseThrow(() -> new TranslationException("Could not find Language for translation."));

        // get default translation from the product
        Translation defaultTranslation = this.translationRepository.getOneByProductAndLanguage(product, defaultLanguage);

        // create new translation with the default content
        Translation translation = new Translation();
        translation.setLanguage(language);
        translation.setShortDescription(defaultTranslation.getShortDescription());
        translation.setLongDescription(defaultTranslation.getLongDescription());

        // let the translator translate it
        translation.setShortDescription(translator.translateText(translation.getShortDescription(), defaultTranslation.getLanguage().getIsoCode(), to));
        translation.setLongDescription(translator.translateText(translation.getLongDescription(), defaultTranslation.getLanguage().getIsoCode(), to));

        // add the new translation to the product
        product.addTranslation(translation);
        translation.setProduct(product);

        // save the translation
        this.translationRepository.save(translation);
        log.info("Auto Translate " + product.getName() + " to " + to);
        return product;
    }

    public Translatable translate(Translatable translatable, String from, String to) {
        return translatable.translate(translator, from, to);
    }

    public String translate(String string, String from, String to) {
        return this.translator.translateText(string, from, to);
    }
}
