package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Description;
import de.bhtberlin.paf2023.productdatatranslation.repo.DescriptionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service for basic CRUD operations on Descriptions.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DescriptionCrudService {

    /**
     * An implementation of a {@link DescriptionRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final DescriptionRepository translationRepository;

    /**
     * Will return a list of all {@link Description Descriptions}.
     * This list may be empty, if no Descriptions are present.
     *
     * @return A List of {@link Description Descriptions}
     */
    public @NonNull List<Description> listAllDescriptions() {
        return this.translationRepository.findAll();
    }

    /**
     * Create a Description.
     *
     * @param description The Description, that should be created.
     * @return The Description, that was created.
     */
    public @NotNull Description createDescription(@NotNull Description description) {
        return this.translationRepository.save(description);
    }

    /**
     * Read a Description.
     *
     * @param id The id for the Description.
     * @return An optional containing the found Description.
     */
    public @NotNull Optional<Description> readDescription(int id) {
        return this.translationRepository.findById(id);
    }

    /**
     * Update a Description.
     *
     * @param description The Description with updated values.
     * @return The updated Description.
     */
    public @NotNull Description updateDescription(@NotNull Description description) {
        return this.translationRepository.save(description);
    }

    /**
     * Delete a Description.
     *
     * @param description The Description to delete.
     */
    public void deleteDescription(@NotNull Description description) {
        this.translationRepository.delete(description);
    }

    /**
     * Delete a Description by its id.
     *
     * @param id The id of the Description to delete.
     */
    public void deleteDescriptionById(int id) {
        this.translationRepository.deleteById(id);
    }
}
