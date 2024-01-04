package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.repo.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Service for basic CRUD operations on products.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCrudService {

    final TranslationService translationService;

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

    public @NonNull List<Product> listAllProducts(@NotNull final String locale) {
        return this.productRepository.findAll().stream().
                peek(product -> {
                    if (!locale.isEmpty()) {
                        product.removeTranslationsNotInLocale(locale);
                        if (!product.hasTranslations()) {
                            this.translationService.translateProduct(product, locale);
                        }
                    }
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

    public @NotNull Optional<Product> readProduct(int id, Locale locale) {
        return this.readProduct(id, locale.toLanguageTag());
    }

    public @NotNull Optional<Product> readProduct(int id, @NotNull String locale) {
        Product product = this.productRepository.findById(id).orElse(null);
        if (product != null && !locale.isEmpty()) {
            boolean hasCorrectTranslation = false;
            for (Translation translation : product.getTranslations()) {
                if (translation.getLanguage().getIsoCode().equalsIgnoreCase(locale)) {
                    hasCorrectTranslation = true;
                }
            }
            if (!hasCorrectTranslation) {
                product = this.translationService.translateProduct(product, locale);
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
     * Delete a product.
     *
     * @param product The product to delete.
     */
    public void deleteProduct(@NotNull Product product) {
        this.productRepository.delete(product);
    }

    /**
     * Delete a product by its id.
     *
     * @param id The id of the product to delete.
     */
    public void deleteProductById(int id) {
        this.productRepository.deleteById(id);
    }
}
