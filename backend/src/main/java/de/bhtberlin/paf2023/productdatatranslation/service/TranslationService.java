package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import de.bhtberlin.paf2023.productdatatranslation.repo.TranslationRepository;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service for basic CRUD operations on Translations.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

    /**
     * An implementation of a {@link TranslationRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final TranslationRepository translationRepository;

    /**
     * The {@link LanguageService} for access to {@link Language Languages}.
     */
    final LanguageService languageService;

    /**
     * An implementation of {@link Translator} that takes care of translation
     * and conversion of text, currencies and measurements.
     */
    final Translator translator;

    /**
     * Will return a list of all {@link Translation Translations}.
     * This list may be empty, if no Translations are present.
     *
     * @return A List of {@link Translation Translations}
     */
    public @NonNull List<Translation> listAllTranslations() {
        return this.translationRepository.findAll();
    }

    /**
     * Create a Translation.
     *
     * @param translation The Translation, that should be created.
     * @return The Translation, that was created.
     */
    public @NotNull Translation createTranslation(@NotNull Translation translation) {
        return this.translationRepository.save(translation);
    }

    /**
     * Read a Translation.
     *
     * @param id The id for the Translation.
     * @return An optional containing the found Translation.
     */
    public @NotNull Optional<Translation> readTranslation(int id) {
        return this.translationRepository.findById(id);
    }

    /**
     * Update a Translation.
     *
     * @param translation The Translation with updated values.
     * @return The updated Translation.
     */
    public @NotNull Translation updateTranslation(@NotNull Translation translation) {
        Translation updated = this.translationRepository.save(translation);
        // check if the translation was in the default language and change
        // all other translations
        if (translation.getLanguage() != null && translation.getLanguage().getIsoCode().equalsIgnoreCase(AppConfig.DEFAULT_LANGUAGE)) {
            updated.getProduct().getTranslations().forEach(t -> {
                if (!t.getLanguage().getIsoCode().equalsIgnoreCase(AppConfig.DEFAULT_LANGUAGE)) {
                    this.translateProduct(updated.getProduct(), t.getLanguage().getIsoCode());
                }
            });
        }
        return updated;
    }

    /**
     * Delete a Translation.
     *
     * @param translation The Translation to delete.
     */
    public void deleteTranslation(@NotNull Translation translation) {
        this.translationRepository.delete(translation);
    }

    /**
     * Delete a Translation by its id.
     *
     * @param id The id of the Translation to delete.
     */
    public void deleteTranslationById(int id) {
        this.translationRepository.deleteById(id);
    }

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
        Language defaultLanguage = this.languageService.getByIsoCode(AppConfig.DEFAULT_LANGUAGE);
        to = LanguageService.normalizeLanguageTag(to);
        Language language = this.languageService.getByIsoCode(to);

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
        translation.setShortDescription(this.translator.translateText(
                translation.getShortDescription(),
                defaultTranslation.getLanguage().getIsoCode(),
                to
        ));
        translation.setLongDescription(this.translator.translateText(
                translation.getLongDescription(),
                defaultTranslation.getLanguage().getIsoCode(),
                to
        ));

        // save the translation
        this.translationRepository.save(translation);
        return product;
    }
}
