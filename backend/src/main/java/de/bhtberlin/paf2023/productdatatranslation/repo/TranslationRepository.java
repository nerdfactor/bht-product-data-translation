package de.bhtberlin.paf2023.productdatatranslation.repo;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Translation} entities.
 */
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Integer> {

    /**
     * Returns the translation for the given product and language.
     *
     * @param product  the product
     * @param language the language
     * @return the translation
     */
    Translation getOneByProductAndLanguage(Product product, Language language);
}
