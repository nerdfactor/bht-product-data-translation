package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.repo.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Service for product search.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductSearchService {

    final TranslationService translationService;

    /**
     * An implementation of a {@link ProductRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final ProductRepository productRepository;

    public @NonNull Page<Product> searchAllProducts(String search, Pageable page) {
        return this.searchAllProducts(search, "", page);
    }

    public @NonNull Page<Product> searchAllProducts(String search, Locale locale, Pageable page) {
        return this.searchAllProducts(search, locale.toLanguageTag(), page);
    }

    /**
     * Search all products containing the search term.
     *
     * @param search The search term.
     * @param locale The locale that the result will be restricted to.
     * @return A {@link Page} with the search results.
     */
    public @NonNull Page<Product> searchAllProducts(String search, String locale, Pageable page) {
        String defaultLanguageSearch = this.translationService.translate(search, locale, AppConfig.DEFAULT_LANGUAGE);
        defaultLanguageSearch = "%" + defaultLanguageSearch.trim().toLowerCase() + "%";
        search = "%" + search.trim().toLowerCase() + "%";
        Page<Product> products = this.productRepository.searchAllProductsMultiSearch(search, defaultLanguageSearch, page);
        products.getContent().forEach(product -> {
            product.removeTranslationsNotInLocale(locale);
            if (!product.hasTranslations()) {
                this.translationService.translateProduct(product, locale);
            }
        });
        return products;
    }

}
