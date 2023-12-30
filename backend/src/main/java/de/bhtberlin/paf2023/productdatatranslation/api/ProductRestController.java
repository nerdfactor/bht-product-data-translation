package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for {@link Product} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

	final ProductCrudService productCrudService;

	final ModelMapper mapper;

	@GetMapping(value = {"", "/"})
	public ResponseEntity<List<ProductDto>> listAllProducts() {
		;
		return ResponseEntity.ok(this.productCrudService.listAllProducts(LocaleContextHolder.getLocale())
				.stream().map(product -> this.mapper.map(product, ProductDto.class))
				.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> readProduct(@PathVariable final int id) throws EntityNotFoundException {
		Product product = this.productCrudService.readProduct(id).
				orElseThrow(() -> new EntityNotFoundException("Product with Id " + id + " was not found."));
		return ResponseEntity.ok(this.mapper.map(product, ProductDto.class));
	}

	/**
	 * Create a new {@link Product} .
	 * <p>
	 * This method will enforce plain {@link Product} creation by removing all linked entities.
	 */
	@PostMapping("")
	public ResponseEntity<ProductDto> createProduct(@RequestBody final ProductDto dto) {
		dto.setCategories(null);
		dto.setColors(null);
		dto.setTranslations(null);
		dto.setPictures(null);
		Product created = this.productCrudService.createProduct(this.mapper.map(dto, Product.class));
		return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, ProductDto.class));
	}

	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
	public ResponseEntity<?> setProduct(@PathVariable final int id, @RequestBody final ProductDto dto) {
		if (id != dto.getId()) {
			return ErrorResponseDto.createResponseEntity(
					HttpStatus.UNPROCESSABLE_ENTITY,
					"Mismatch between provided Ids."
			);
		}
		Product updated = this.productCrudService.updateProduct(this.mapper.map(dto, Product.class));
		return ResponseEntity.ok(this.mapper.map(updated, ProductDto.class));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable final int id) {
		this.productCrudService.deleteProductById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public HttpEntity<ErrorResponseDto> handleEntityNotFoundException(HttpRequest request, Exception e) {
		return ErrorResponseDto.createResponseEntity(
				HttpStatus.NOT_FOUND,
				e.getMessage()
		);
	}
}
