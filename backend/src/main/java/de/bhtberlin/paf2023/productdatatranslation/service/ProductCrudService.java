package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import de.bhtberlin.paf2023.productdatatranslation.repo.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Service for basic CRUD operations on products.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCrudService {

    final TranslationService translationService;

    final ModelMapper mapper;

    /**
     * An implementation of a {@link ProductRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final ProductRepository productRepository;

    /**
     * Will return a list of all {@link Product products}.
     * This list may be empty, if no products are present.
     *
     * @return A List of {@link Product Products}
     */
    public @NonNull List<Product> listAllProducts() {
        return this.listAllProducts("");
    }

    public @NonNull List<Product> listAllProducts(@NotNull Locale locale) {
        return this.listAllProducts(locale.toLanguageTag());
    }

    /**
     * Will return a list of all {@link Product products}.
     * This list may be empty, if no products are present.
     * Products will be translated into the given locale.
     *
     * @param locale The locale for the products.
     * @return A List of {@link Product Products}
     */
    public @NonNull List<Product> listAllProducts(@NotNull final String locale) {
        final String tag = LanguageService.normalizeLanguageTag(locale);
        return this.productRepository.findAll().stream().
                map(product -> {
                    if (!tag.isEmpty()) {
                        product.removeTranslationsNotInLocale(tag);
                        if (!product.hasTranslations()) {
                            try {
                                this.translationService.translateProduct(product, tag);
                                log.info("Auto translated Product: " + product.getName() + " into " + tag);
                            } catch (TranslationException e) {
                                // could not be translated automatically and can remain empty.
                            }

                        }
                    }
                    return product;
                })
                .toList();
    }

    /**
     * Create a product.
     *
     * @param product The product, that should be created.
     * @return The product, that was created.
     */
    public @NotNull Product createProduct(@NotNull Product product) {
        return this.productRepository.save(product);
    }

    /**
     * Read a product.
     *
     * @param id The id for the product.
     * @return An optional containing the found product.
     */
    public @NotNull Optional<Product> readProduct(int id) {
        return this.readProduct(id, "");
    }

    /**
     * Read a product.
     *
     * @param id     The id for the product.
     * @param locale The locale for the product.
     * @return An optional containing the found product.
     */
    public @NotNull Optional<Product> readProduct(int id, Locale locale) {
        return this.readProduct(id, locale.toLanguageTag());
    }

    /**
     * Read a product.
     *
     * @param id     The id for the product.
     * @param locale The locale for the product.
     * @return An optional containing the found product.
     */
    public @NotNull Optional<Product> readProduct(int id, @NotNull String locale) {
        final String tag = LanguageService.normalizeLanguageTag(locale);
        Product product = this.productRepository.findById(id).orElse(null);
        if (product != null && !tag.isEmpty()) {
            boolean hasCorrectTranslation = false;
            for (Translation translation : product.getTranslations()) {
                if (translation.getLanguage().getIsoCode().equalsIgnoreCase(tag)) {
                    hasCorrectTranslation = true;
                }
            }
            if (!hasCorrectTranslation) {
                try {
                    product = this.translationService.translateProduct(product, tag);
                    log.info("Auto translated Product: " + product.getName() + " into " + tag);
                } catch (TranslationException e) {
                    // could not be translated automatically and can remain empty.
                }
            }
        }
        return Optional.ofNullable(product);
    }

    /**
     * Update a product.
     *
     * @param product The product with updated values.
     * @return The updated product.
     */
    public @NotNull Product updateProduct(@NotNull Product product) {
        return this.productRepository.save(product);
    }

    /**
     * Merge a product with existing relationships.
     *
     * @param dto The product with updated values.
     * @return The updated product.
     */
    public @NotNull ProductDto mergeWithExistingRelationships(@NotNull ProductDto dto) {
        Product entity = this.readProduct(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Product with Id " + dto.getId() + " was not found."));
        if (dto.getCategories() == null) {
            // if categories where not send, fill with existing relations.
            dto.setCategories(Optional.ofNullable(entity.getCategories()).orElse(new HashSet<>()).stream().map(c -> new CategoryDto(c.getId())).collect(Collectors.toSet()));
        }
        if (dto.getColors() == null) {
            // if colors where not send, fill with existing relations.
            dto.setColors(Optional.ofNullable(entity.getColors()).orElse(new HashSet<>()).stream().map(c -> new ColorDto(c.getId())).collect(Collectors.toSet()));
        }
        if (dto.getPictures() != null) {
            dto.getPictures().stream().filter(Objects::nonNull).forEach(pictureDto -> pictureDto.setProduct(new ProductDto(dto.getId())));
            entity.getPictures().forEach(picture -> {
                if (dto.getPictures().stream().filter(Objects::nonNull).noneMatch(pictureDto -> pictureDto.getId() == picture.getId())) {
                    picture.setProduct(null);
                }
            });
        }
        if (dto.getTranslations() != null) {
            dto.getTranslations().stream().filter(Objects::nonNull).forEach(translationDto -> translationDto.setProduct(new ProductDto(dto.getId())));
            entity.getTranslations().forEach(translation -> {
                if (dto.getTranslations().stream().filter(Objects::nonNull).noneMatch(translationDto -> translationDto.getId() == translation.getId())) {
                    translation.setProduct(null);
                }
            });
        }
        return dto;
    }

    /**
     * Delete a product.
     *
     * @param product The product to delete.
     */
    public void deleteProduct(@NotNull Product product) {
        if(product.getTranslations() != null) {
            product.getTranslations().forEach(translation -> translation.setProduct(null));
        }
        product.setTranslations(null);
        product.setCategories(null);
        product.setColors(null);
        if(product.getPictures() != null){
            product.getPictures().forEach(picture -> picture.setProduct(null));
        }
        product.setPictures(null);
        this.productRepository.save(product);
        this.productRepository.delete(product);
    }

    /**
     * Delete a product by its id.
     *
     * @param id The id of the product to delete.
     */
    public void deleteProductById(int id) {
        Product product = this.productRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Product with Id " + id + " was not found."));
        this.deleteProduct(product);
    }
}
