package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import de.bhtberlin.paf2023.productdatatranslation.repo.TranslationRepository;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.SimpleStringTranslator;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AutoTranslationService {

	final TranslationRepository translationRepository;

	final LanguageRepository languageRepository;

	// todo: placeholder for auto translation
	public Product autoTranslateProductAsync(Product product, String locale) {
		Language language = this.languageRepository.findOneByIsoCode(locale).orElseThrow();

		// get default translation from the product
		Translation defaultTranslation = this.translationRepository.getOneByProduct(product);

		// create new translation with the default content
		Translation translation = new Translation();
		translation.setLanguage(language);
		translation.setShortDescription(defaultTranslation.getShortDescription());
		translation.setLongDescription(defaultTranslation.getLongDescription());

		// let the translator translate it
		Translator translator = new SimpleStringTranslator();
		translation.setShortDescription(translator.translate(translation.getShortDescription(), locale));
		translation.setLongDescription(translator.translate(translation.getLongDescription(), locale));

		// add the new translation to the product
		product.addTranslation(translation);
		translation.setProduct(product);

		// save the translation
		this.translationRepository.save(translation);
		log.info("Auto Translate " + product.getName() + " to " + locale);
		return product;
	}

	public String autoTranslateString(String string, String locale) {
		Translator translator = new SimpleStringTranslator();
		return translator.translate(string, locale);
	}
}
