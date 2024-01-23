package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Category;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for {@link Category} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    /**
     * The {@link CategoryService} for access to {@link Category Categories}.
     */
    final CategoryService categoryService;

    /**
     * The {@link ModelMapper} used for mapping between Entity and DTOs.
     */
    final ModelMapper mapper;

    /**
     * List all {@link Category Categories}.
     *
     * @return A {@link ResponseEntity} containing a list of {@link CategoryDto} objects.
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<CategoryDto>> listAllCategories() {
        return ResponseEntity.ok(this.categoryService.listAllCategories()
                .stream().map(category -> this.mapper.map(category, CategoryDto.class))
                .toList());
    }

    /**
     * Read a single {@link Category} by its ID.
     *
     * @param id The ID of the {@link Category} to read.
     * @return A {@link ResponseEntity} containing a {@link CategoryDto} object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> readCategory(@PathVariable final int id) {
        Category category = this.categoryService.readCategory(id).
                orElseThrow(() -> new EntityNotFoundException("Category with Id " + id + " was not found."));
        return ResponseEntity.ok(this.mapper.map(category, CategoryDto.class));
    }

    /**
     * Create a new {@link Category}.
     *
     * @param dto The {@link CategoryDto} to create.
     * @return A {@link ResponseEntity} containing a {@link CategoryDto} object.
     */
    @PostMapping("")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody final CategoryDto dto) {
        Category created = this.categoryService.createCategory(this.mapper.map(dto, Category.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, CategoryDto.class));
    }

    /**
     * Update an existing {@link Category}.
     *
     * @param id  The ID of the {@link Category} to update.
     * @param dto The {@link CategoryDto} to update.
     * @return A {@link ResponseEntity} containing a {@link CategoryDto} object.
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<CategoryDto> setCategory(@PathVariable final int id, @RequestBody final CategoryDto dto) {
        if (id != dto.getId()) {
            throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
        }
        Category updated = this.categoryService.updateCategory(this.mapper.map(dto, Category.class));
        return ResponseEntity.ok(this.mapper.map(updated, CategoryDto.class));
    }

    /**
     * Delete an existing {@link Category} .
     *
     * @param id The ID of the {@link Category} to delete.
     * @return A {@link ResponseEntity} with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final int id) {
        this.categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
