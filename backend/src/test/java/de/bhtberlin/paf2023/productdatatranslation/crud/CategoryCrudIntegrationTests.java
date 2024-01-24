package de.bhtberlin.paf2023.productdatatranslation.crud;

import de.bhtberlin.paf2023.productdatatranslation.entity.Category;
import de.bhtberlin.paf2023.productdatatranslation.service.CategoryService;
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
 * Tests for basic crud operations of {@link Category Categories}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryCrudIntegrationTests {

    @Autowired
    CategoryService categoryService;

    /**
     * Check if a Category can be read after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void categoryCanBeCreatedAndRead() {
        Category create = this.createTestCategory();
        Category created = categoryService.createCategory(create);
        assertNotNull(created);
        assertNotEquals(0, created.getId());
        assertEquals(create.getName(), created.getName());

        Optional<Category> read = this.categoryService.readCategory(created.getId());
        assertTrue(read.isPresent());
        assertEquals(read.get().getName(), create.getName());
    }

    /**
     * Check if a Category can be updated after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void categoryCanBeUpdated() {
        Category create = this.createTestCategory();
        Category created = categoryService.createCategory(create);
        assertNotNull(created);

        String updatedCategoryName = "Geänderter Name";
        created.setName(updatedCategoryName);
        Category updated = this.categoryService.updateCategory(created);

        assertNotNull(updated);
        assertEquals(updatedCategoryName, updated.getName());
    }

    /**
     * Check if a Category can't be read after it was deleted.
     */
    @Test
    @Transactional
    @Rollback
    public void categoryCantBeReadAfterItWasDeleted() {
        Category create1 = this.createTestCategory();
        Category created1 = categoryService.createCategory(create1);
        assertNotNull(created1);

        this.categoryService.deleteCategory(created1);

        Optional<Category> read1 = this.categoryService.readCategory(created1.getId());
        assertFalse(read1.isPresent());

        Category create2 = this.createTestCategory();
        Category created2 = categoryService.createCategory(create2);
        assertNotNull(created2);

        this.categoryService.deleteCategoryById(created2.getId());

        Optional<Category> read2 = this.categoryService.readCategory(created2.getId());
        assertFalse(read2.isPresent());
    }

    /**
     * Checks if Categories can be listed after they are created.
     */
    @Test
    @Transactional
    @Rollback
    public void categoriesCanBeListed() {
        int previousAmountOfCategories = this.categoryService.listAllCategories().size();
        int amountOfCategories = 10;
        List<Category> listOfTestCategories = new ArrayList<>();
        for (int i = 0; i < amountOfCategories; i++) {
            Category create = this.createTestCategory();
            listOfTestCategories.add(create);
            categoryService.createCategory(create);
        }
        Category firstCategory = listOfTestCategories.stream()
                .min(Comparator.comparing(Category::getName))
                .orElse(this.createTestCategory());

        List<Category> categories = this.categoryService.listAllCategories();
        assertNotNull(categories);
        assertEquals(amountOfCategories + previousAmountOfCategories, categories.size());
        assertEquals(firstCategory.getName(), categories.stream().
                min(Comparator.comparing(Category::getName))
                .orElseThrow()
                .getName()
        );
    }

    /**
     * Create a random {@link Category} for testing.
     *
     * @return A Category with random values.
     */
    private Category createTestCategory() {
        return new Category(
                UUID.randomUUID().toString().replace("-", "").substring(10)
        );
    }

}
