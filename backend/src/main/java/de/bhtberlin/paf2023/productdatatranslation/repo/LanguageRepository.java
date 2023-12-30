package de.bhtberlin.paf2023.productdatatranslation.repo;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

	public Optional<Language> findOneByIsoCode(String isoCode);
}
