package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service for basic CRUD operations on Languages.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LanguageCrudService {

    /**
     * An implementation of a {@link LanguageRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final LanguageRepository languageRepository;

    /**
     * Will return a list of all {@link Language Languages}.
     * This list may be empty, if no Languages are present.
     *
     * @return A List of {@link Language Languages}
     */
    public @NonNull List<Language> listAllLanguages() {
        return this.languageRepository.findAll();
    }

    /**
     * Create a Language.
     *
     * @param Language The Language, that should be created.
     * @return The Language, that was created.
     */
    public @NotNull Language createLanguage(@NotNull Language Language) {
        return this.languageRepository.save(Language);
    }

    /**
     * Read a Language.
     *
     * @param id The id for the Language.
     * @return An optional containing the found Language.
     */
    public @NotNull Optional<Language> readLanguage(int id) {
        return this.languageRepository.findById(id);
    }

    /**
     * Update a Language.
     *
     * @param Language The Language with updated values.
     * @return The updated Language.
     */
    public @NotNull Language updateLanguage(@NotNull Language Language) {
        return this.languageRepository.save(Language);
    }

    /**
     * Delete a Language.
     *
     * @param Language The Language to delete.
     */
    public void deleteLanguage(@NotNull Language Language) {
        this.languageRepository.delete(Language);
    }

    /**
     * Delete a Language by its id.
     *
     * @param id The id of the Language to delete.
     */
    public void deleteLanguageById(int id) {
        this.languageRepository.deleteById(id);
    }
}
