package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Revision;
import de.bhtberlin.paf2023.productdatatranslation.repo.RevisionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service for basic CRUD operations on Revisions.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RevisionCrudService {

    /**
     * An implementation of a {@link RevisionRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final RevisionRepository revisionRepository;

    /**
     * Will return a list of all {@link Revision Revisions}.
     * This list may be empty, if no Revisions are present.
     *
     * @return A List of {@link Revision Revisions}
     */
    public @NonNull List<Revision> listAllRevisions() {
        return this.revisionRepository.findAll();
    }

    /**
     * Create a Revision.
     *
     * @param revision The Revision, that should be created.
     * @return The Revision, that was created.
     */
    public @NotNull Revision createRevision(@NotNull Revision revision) {
        return this.revisionRepository.save(revision);
    }

    /**
     * Read a Revision.
     *
     * @param id The id for the Revision.
     * @return An optional containing the found Revision.
     */
    public @NotNull Optional<Revision> readRevision(int id) {
        return this.revisionRepository.findById(id);
    }

    /**
     * Update a Revision.
     *
     * @param revision The Revision with updated values.
     * @return The updated Revision.
     */
    public @NotNull Revision updateRevision(@NotNull Revision revision) {
        return this.revisionRepository.save(revision);
    }

    /**
     * Delete a Revision.
     *
     * @param revision The Revision to delete.
     */
    public void deleteRevision(@NotNull Revision revision) {
        this.revisionRepository.delete(revision);
    }

    /**
     * Delete a Revision by its id.
     *
     * @param id The id of the Revision to delete.
     */
    public void deleteRevisionById(int id) {
        this.revisionRepository.deleteById(id);
    }
}
