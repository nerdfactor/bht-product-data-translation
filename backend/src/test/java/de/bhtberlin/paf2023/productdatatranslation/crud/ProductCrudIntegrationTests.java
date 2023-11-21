package de.bhtberlin.paf2023.productdatatranslation.crud;

import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for basic crud operations of {@link Product products}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCrudIntegrationTests {

    @Autowired
    ProductCrudService productService;

    /**
     * Check if a product can be read after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void productCanBeCreatedAndRead() {
        Product create = this.createTestProduct();
        Product created = productService.createProduct(create);
        assertNotNull(created);
        assertNotEquals(0, created.getId());
        assertEquals(create.getSerial(), created.getSerial());

        Optional<Product> read = this.productService.readProduct(created.getId());
        assertTrue(read.isPresent());
        assertEquals(read.get().getSerial(), create.getSerial());
    }

    /**
     * Check if a product can be updated after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void productCanBeUpdated() {
        Product create = this.createTestProduct();
        Product created = productService.createProduct(create);
        assertNotNull(created);

        String updatedProductName = "Geänderter Name";
        created.setName(updatedProductName);
        Product updated = this.productService.updateProduct(created);

        assertNotNull(updated);
        assertEquals(updatedProductName, updated.getName());
    }

    /**
     * Check if a product can't be read after it was deleted.
     */
    @Test
    @Transactional
    @Rollback
    public void productCantBeReadAfterItWasDeleted() {
        Product create1 = this.createTestProduct();
        Product created1 = productService.createProduct(create1);
        assertNotNull(created1);

        this.productService.deleteProduct(created1);

        Optional<Product> read1 = this.productService.readProduct(created1.getId());
        assertFalse(read1.isPresent());

        Product create2 = this.createTestProduct();
        Product created2 = productService.createProduct(create2);
        assertNotNull(created2);

        this.productService.deleteProductById(created2.getId());

        Optional<Product> read2 = this.productService.readProduct(created2.getId());
        assertFalse(read2.isPresent());
    }

    /**
     * Checks if products can be listed after they are created.
     */
    @Test
    @Transactional
    @Rollback
    public void productsCanBeListed() {
        int amountOfProducts = 10;
        List<Product> listOfTestProducts = new ArrayList<>();
        for (int i = 0; i < amountOfProducts; i++) {
            Product create = this.createTestProduct();
            listOfTestProducts.add(create);
            productService.createProduct(create);
        }
        Product firstProduct = listOfTestProducts.stream()
                .min(Comparator.comparing(Product::getName))
                .orElse(this.createTestProduct());

        List<Product> products = this.productService.listAllProducts();
        assertNotNull(products);
        assertEquals(amountOfProducts, products.size());
        assertEquals(firstProduct.getName(), products.stream().
                min(Comparator.comparing(Product::getName))
                .orElseThrow()
                .getName()
        );
    }

    /**
     * Create a random {@link Product} for testing.
     *
     * @return A Product with random values.
     */
    private Product createTestProduct() {
        Random random = new Random();
        return new Product(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString().replace("-", "").substring(10),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble()
        );
    }

}
