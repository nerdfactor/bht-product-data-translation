package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Category;
import de.bhtberlin.paf2023.productdatatranslation.repo.CategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service for basic CRUD operations on Categories.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryCrudService {

    /**
     * An implementation of a {@link CategoryRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final CategoryRepository categoryRepository;

    /**
     * Will return a list of all {@link Category Categories}.
     * This list may be empty, if no Categories are present.
     *
     * @return A List of {@link Category Categories}
     */
    public @NonNull List<Category> listAllCategories() {
        return this.categoryRepository.findAll();
    }

    /**
     * Create a Category.
     *
     * @param category The Category, that should be created.
     * @return The Category, that was created.
     */
    public @NotNull Category createCategory(@NotNull Category category) {
        return this.categoryRepository.save(category);
    }

    /**
     * Read a Category.
     *
     * @param id The id for the Category.
     * @return An optional containing the found Category.
     */
    public @NotNull Optional<Category> readCategory(int id) {
        return this.categoryRepository.findById(id);
    }

    /**
     * Update a Category.
     *
     * @param category The Category with updated values.
     * @return The updated Category.
     */
    public @NotNull Category updateCategory(@NotNull Category category) {
        return this.categoryRepository.save(category);
    }

    /**
     * Delete a Category.
     *
     * @param category The Category to delete.
     */
    public void deleteCategory(@NotNull Category category) {
        this.categoryRepository.delete(category);
    }

    /**
     * Delete a Category by its id.
     *
     * @param id The id of the Category to delete.
     */
    public void deleteCategoryById(int id) {
        this.categoryRepository.deleteById(id);
    }
}
