package de.bhtberlin.paf2023.productdatatranslation.repo;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Language} entities.
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    /**
     * Finds a language by its ISO code.
     *
     * @param isoCode The ISO code of the language.
     * @return The language with the given ISO code.
     */
    Optional<Language> findOneByIsoCode(String isoCode);
}
