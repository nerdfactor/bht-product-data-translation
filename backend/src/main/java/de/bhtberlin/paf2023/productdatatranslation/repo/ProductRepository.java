package de.bhtberlin.paf2023.productdatatranslation.repo;

import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    /**
     * Search products containing a search term in
     * - name
     * - Translations shortDescription
     * - Translations longDescription
     *
     * @param search   The search term.
     * @param pageable The {@link Pageable} restricting the amount of results.
     * @return A {@link Page} containing the results.
     */
    @Query("SELECT product FROM Product product " +
            "LEFT JOIN Translation translation ON translation.product = product " +
            "WHERE lower(product.name) LIKE :search " +
            "OR lower(translation.shortDescription) LIKE :search " +
            "OR lower(translation.longDescription) LIKE :search")
    Page<Product> searchAllProducts(String search, Pageable pageable);

    /**
     * Search products containing a search term in
     * - name
     * - Translations shortDescription
     * - Translations longDescription
     * Takes an additional search term that is translated in the default language
     * in order to find products that are not translated yet.
     *
     * @param search                The search term.
     * @param defaultLanguageSearch The search term translated in the default language.
     * @param pageable              The {@link Pageable} restricting the amount of results.
     * @return A {@link Page} containing the results.
     */
    @Query("SELECT product FROM Product product " +
            "LEFT JOIN Translation translation ON translation.product = product " +
            "WHERE lower(product.name) LIKE :search " +
            "OR lower(translation.shortDescription) LIKE :search " +
            "OR lower(translation.longDescription) LIKE :search " +
            "OR lower(product.name) LIKE :defaultLanguageSearch " +
            "OR lower(translation.shortDescription) LIKE :defaultLanguageSearch " +
            "OR lower(translation.longDescription) LIKE :defaultLanguageSearch")
    Page<Product> searchAllProductsMultiSearch(String search, String defaultLanguageSearch, Pageable pageable);
}
