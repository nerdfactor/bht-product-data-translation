package de.bhtberlin.paf2023.productdatatranslation.service;

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
     * @param Translation The Translation, that should be created.
     * @return The Translation, that was created.
     */
    public @NotNull Translation createTranslation(@NotNull Translation Translation) {
        return this.translationRepository.save(Translation);
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
     * @param Translation The Translation with updated values.
     * @return The updated Translation.
     */
    public @NotNull Translation updateTranslation(@NotNull Translation Translation) {
        return this.translationRepository.save(Translation);
    }

    /**
     * Delete a Translation.
     *
     * @param Translation The Translation to delete.
     */
    public void deleteTranslation(@NotNull Translation Translation) {
        this.translationRepository.delete(Translation);
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
