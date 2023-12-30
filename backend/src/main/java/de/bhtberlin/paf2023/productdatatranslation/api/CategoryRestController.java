package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Category;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.CategoryCrudService;
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

	final CategoryCrudService categoryCrudService;

	final ModelMapper mapper;

	@GetMapping(value = {"", "/"})
	public ResponseEntity<List<CategoryDto>> listAllCategories() {
		return ResponseEntity.ok(this.categoryCrudService.listAllCategories()
				.stream().map(category -> this.mapper.map(category, CategoryDto.class))
				.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> readCategory(@PathVariable final int id) {
		Category category = this.categoryCrudService.readCategory(id).
				orElseThrow(() -> new EntityNotFoundException("Category with Id " + id + " was not found."));
		return ResponseEntity.ok(this.mapper.map(category, CategoryDto.class));
	}

	/**
	 * Create a new {@link Category} .
	 * <p>
	 * This method will enforce plain {@link Category} creation by removing all linked entities.
	 */
	@PostMapping("")
	public ResponseEntity<CategoryDto> createCategory(@RequestBody final CategoryDto dto) {
		Category created = this.categoryCrudService.createCategory(this.mapper.map(dto, Category.class));
		return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, CategoryDto.class));
	}

	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
	public ResponseEntity<CategoryDto> setCategory(@PathVariable final int id, @RequestBody final CategoryDto dto) {
		if (id != dto.getId()) {
			throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
		}
		Category updated = this.categoryCrudService.updateCategory(this.mapper.map(dto, Category.class));
		return ResponseEntity.ok(this.mapper.map(updated, CategoryDto.class));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable final int id) {
		this.categoryCrudService.deleteCategoryById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
