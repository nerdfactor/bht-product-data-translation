package de.bhtberlin.paf2023.productdatatranslation.api;

import com.google.cloud.translate.Translation;
import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Category;
import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return ResponseEntity.ok(this.productCrudService.listAllProducts(LocaleContextHolder.getLocale())
                .stream().map(product -> this.mapper.map(product, ProductDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> readProduct(@PathVariable final int id) {
        Product product = this.productCrudService.readProduct(id, LocaleContextHolder.getLocale()).
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

    /**
     * Set a {@link Product} to the data in the DTO. This will override the data
     * of the existing entity. So make sure the DTO contains everything.
     * <br>
     * Update connected {@link Translation Translations} from other side of relationship.
     *
     * @param id  The id of the {@link Product}.
     * @param dto The {@link ProductDto} containing the new data.
     * @return The updated {@link ProductDto}.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDto> setProduct(@PathVariable final int id, @RequestBody final ProductDto dto) {
        if (id != dto.getId()) {
            throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
        }
        Product updated = this.productCrudService.updateProduct(this.mapper.map(dto, Product.class));
        return ResponseEntity.ok(this.mapper.map(updated, ProductDto.class));
    }

    /**
     * Update a {@link Product} with the data in the DTO. It will merge the data
     * with the existing entity.
     * <br>
     * If the DTO contains now lists for {@link Category Categories} or {@link Color Colors}
     * they will be filled with the existing relationships. To reset the relationships
     * send an empty array (to clear all) or an array containing all the desired
     * relations.
     * <br>
     * Update connected {@link Translation Translations} from other side of relationship.
     *
     * @param id  The id of the {@link Product}.
     * @param dto The {@link ProductDto} containing the updated data.
     * @return The updated {@link ProductDto}.
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable final int id, @RequestBody final ProductDto dto) {
        if (id != dto.getId()) {
            throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
        }
        Product entity = this.productCrudService.readProduct(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Product with Id " + id + " was not found."));
        if (dto.getCategories() == null) {
            // if categories where not send, fill with existing relations.
            dto.setCategories(Optional.ofNullable(entity.getCategories()).orElse(new HashSet<>()).stream().map(c -> new CategoryDto(c.getId())).collect(Collectors.toSet()));
        }
        if (dto.getColors() == null) {
            // if colors where not send, fill with existing relations.
            dto.setColors(Optional.ofNullable(entity.getColors()).orElse(new HashSet<>()).stream().map(c -> new ColorDto(c.getId())).collect(Collectors.toSet()));
        }
        Product updated = this.productCrudService.updateProduct(this.mapper.map(dto, Product.class));
        return ResponseEntity.ok(this.mapper.map(updated, ProductDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final int id) {
        this.productCrudService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
