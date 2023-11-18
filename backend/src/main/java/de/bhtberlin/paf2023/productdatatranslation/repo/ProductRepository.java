package de.bhtberlin.paf2023.productdatatranslation.repo;

import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
