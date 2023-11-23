package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.AddNewProductDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.*;
import de.bhtberlin.paf2023.productdatatranslation.service.CategoryCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslationCrudService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

/**
 * Controller for {@link Product} related user stories.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    final EntityManager entityManager;

    final ProductCrudService productCrudService;

    final TranslationCrudService translationCrudService;

    final CategoryCrudService categoryCrudService;

    final ModelMapper mapper;


    /**
     * Controller for the "AddNewProductIntoSystem" user story.
     * It will take information about a {@link Product} with descriptions in german
     * and assigned {@link Color Colors} and {@link Category Categories}.
     * New {@link Category Categories} will be created.
     * <p>
     * Should be used instead of {@link ProductRestController#createProduct(ProductDto) the REST endpoint} for plain
     * creation of {@link Product Products}.
     *
     * @param dto A data transfer object containing all required data to add a new {@link Product}.
     * @return The created {@link Product}.
     */
    @PostMapping("/add")
    public ResponseEntity<ProductDto> addNewProduct(@RequestBody final AddNewProductDto dto) {
        // todo: Test.
        // todo: add validation, either in code or with Spring Validation?
        // todo: refactor into separate component for "cleaner" architecture?
        Product product = this.mapper.map(dto, Product.class);
        Translation translation = this.translationCrudService.createTranslation(new Translation(
                dto.getShortDescription(),
                dto.getLongDescription(),
                this.entityManager.getReference(Language.class, dto.getLanguage().getId())
        ));
        translation.setProduct(product);
        product.addTranslation(translation);
        product.setColors(new HashSet<>());
        dto.getColors().forEach(color ->
                product.addColor(this.entityManager.getReference(Color.class, color.getId()))
        );
        product.setCategories(new HashSet<>());
        dto.getExistingCategories().forEach(category ->
                product.addCategory(this.entityManager.getReference(Category.class, category.getId()))
        );
        dto.getAddedCategories().forEach(name ->
                product.addCategory(this.categoryCrudService.createCategory(new Category(name)))
        );
        this.productCrudService.createProduct(product);
        return ResponseEntity.ok(this.mapper.map(product, ProductDto.class));
    }
}
