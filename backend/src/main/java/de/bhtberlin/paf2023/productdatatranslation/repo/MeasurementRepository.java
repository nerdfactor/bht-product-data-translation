package de.bhtberlin.paf2023.productdatatranslation.repo;

import de.bhtberlin.paf2023.productdatatranslation.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Measurement} entities.
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
}
