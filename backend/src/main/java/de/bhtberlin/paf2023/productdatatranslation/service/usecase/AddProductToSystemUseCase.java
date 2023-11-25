package de.bhtberlin.paf2023.productdatatranslation.service.usecase;

import de.bhtberlin.paf2023.productdatatranslation.dto.AddNewProductDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.*;
import de.bhtberlin.paf2023.productdatatranslation.service.CategoryCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslationCrudService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * UseCase for the "AddNewProductIntoSystem" user story.
 * It will take information about a {@link Product} with descriptions in german
 * and assigned {@link Color Colors} and {@link Category Categories}.
 * New {@link Category Categories} will be created.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AddProductToSystemUseCase implements UseCase<AddNewProductDto, ProductDto> {

    final EntityManager entityManager;

    final ProductCrudService productCrudService;

    final TranslationCrudService translationCrudService;

    final CategoryCrudService categoryCrudService;

    final ModelMapper mapper;

    /**
     * Executes the UseCase.
     *
     * @param dto A data transfer object containing all required data to add a new {@link Product}.
     * @return The created {@link ProductDto}.
     */
    public @NotNull ProductDto execute(@NotNull AddNewProductDto dto) {
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
        return this.mapper.map(product, ProductDto.class);
    }
}
