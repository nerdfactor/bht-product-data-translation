package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.repo.ColorRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service for basic CRUD operations on Colors.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ColorCrudService {

    /**
     * An implementation of a {@link ColorRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final ColorRepository colorRepository;

    /**
     * Will return a list of all {@link Color Colors}.
     * This list may be empty, if no Colors are present.
     *
     * @return A List of {@link Color Colors}
     */
    public @NonNull List<Color> listAllColors() {
        return this.colorRepository.findAll();
    }

    /**
     * Create a Color.
     *
     * @param Color The Color, that should be created.
     * @return The Color, that was created.
     */
    public @NotNull Color createColor(@NotNull Color Color) {
        return this.colorRepository.save(Color);
    }

    /**
     * Read a Color.
     *
     * @param id The id for the Color.
     * @return An optional containing the found Color.
     */
    public @NotNull Optional<Color> readColor(int id) {
        return this.colorRepository.findById(id);
    }

    /**
     * Update a Color.
     *
     * @param Color The Color with updated values.
     * @return The updated Color.
     */
    public @NotNull Color updateColor(@NotNull Color Color) {
        return this.colorRepository.save(Color);
    }

    /**
     * Delete a Color.
     *
     * @param Color The Color to delete.
     */
    public void deleteColor(@NotNull Color Color) {
        this.colorRepository.delete(Color);
    }

    /**
     * Delete a Color by its id.
     *
     * @param id The id of the Color to delete.
     */
    public void deleteColorById(int id) {
        this.colorRepository.deleteById(id);
    }
}
