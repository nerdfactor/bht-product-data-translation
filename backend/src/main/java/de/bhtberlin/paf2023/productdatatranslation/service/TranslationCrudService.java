package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.repo.TranslationRepository;
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
public class TranslationCrudService {

    /**
     * An implementation of a {@link TranslationRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final TranslationRepository translationRepository;

    final TranslationService translationService;

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
            // todo: should TranslationService take care of Product translation or delegate to ProductService?
            updated.getProduct().getTranslations().forEach(t -> {
                if (!t.getLanguage().getIsoCode().equalsIgnoreCase(AppConfig.DEFAULT_LANGUAGE)) {
                    this.translationService.translateProduct(updated.getProduct(), t.getLanguage().getIsoCode());
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
}
