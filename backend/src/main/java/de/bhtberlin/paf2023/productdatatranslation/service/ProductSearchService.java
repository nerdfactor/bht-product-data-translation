package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.repo.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Locale;

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

    public @NonNull Page<Product> searchAllProducts(final String search) {
        return this.searchAllProducts(search, "");
    }

    public @NonNull Page<Product> searchAllProducts(final String search, Locale locale) {
        return this.searchAllProducts(search, locale.toLanguageTag());
    }

    public @NonNull Page<Product> searchAllProducts(final String search, String locale) {
        Specification<Product> spec = Specification.where(null);
        spec.and((root, query, cb) -> cb.like(root.get("name"), search));
        Page<Product> products = this.productRepository.findAll(spec, Pageable.unpaged());
        products.getContent().forEach(product -> {
            product.removeTranslationsNotInLocale(locale);
            if (!product.hasTranslations()) {
                this.translationService.translateProduct(product, locale);
            }
        });
        return products;
    }

}
