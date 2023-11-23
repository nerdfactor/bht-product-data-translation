package de.bhtberlin.paf2023.productdatatranslation.crud;

import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslationCrudService;
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
 * Tests for basic crud operations of {@link Translation Translations}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TranslationCrudIntegrationTests {

    @Autowired
    TranslationCrudService translationService;

    /**
     * Check if a Translation can be read after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void TranslationCanBeCreatedAndRead() {
        Translation create = this.createTestTranslation();
        Translation created = translationService.createTranslation(create);
        assertNotNull(created);
        assertNotEquals(0, created.getId());
        assertEquals(create.getShortDescription(), created.getShortDescription());

        Optional<Translation> read = this.translationService.readTranslation(created.getId());
        assertTrue(read.isPresent());
        assertEquals(read.get().getShortDescription(), create.getShortDescription());
    }

    /**
     * Check if a Translation can be updated after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void TranslationCanBeUpdated() {
        Translation create = this.createTestTranslation();
        Translation created = translationService.createTranslation(create);
        assertNotNull(created);

        String updatedTranslationName = "Geänderter Name";
        created.setShortDescription(updatedTranslationName);
        Translation updated = this.translationService.updateTranslation(created);

        assertNotNull(updated);
        assertEquals(updatedTranslationName, updated.getShortDescription());
    }

    /**
     * Check if a Translation can't be read after it was deleted.
     */
    @Test
    @Transactional
    @Rollback
    public void TranslationCantBeReadAfterItWasDeleted() {
        Translation create1 = this.createTestTranslation();
        Translation created1 = translationService.createTranslation(create1);
        assertNotNull(created1);

        this.translationService.deleteTranslation(created1);

        Optional<Translation> read1 = this.translationService.readTranslation(created1.getId());
        assertFalse(read1.isPresent());

        Translation create2 = this.createTestTranslation();
        Translation created2 = translationService.createTranslation(create2);
        assertNotNull(created2);

        this.translationService.deleteTranslationById(created2.getId());

        Optional<Translation> read2 = this.translationService.readTranslation(created2.getId());
        assertFalse(read2.isPresent());
    }

    /**
     * Checks if Translations can be listed after they are created.
     */
    @Test
    @Transactional
    @Rollback
    public void TranslationsCanBeListed() {
        // todo: handle previously existing objects.
        int amountOfTranslations = 10;
        List<Translation> listOfTestTranslations = new ArrayList<>();
        for (int i = 0; i < amountOfTranslations; i++) {
            Translation create = this.createTestTranslation();
            listOfTestTranslations.add(create);
            translationService.createTranslation(create);
        }
        Translation firstTranslation = listOfTestTranslations.stream()
                .min(Comparator.comparing(Translation::getShortDescription))
                .orElse(this.createTestTranslation());

        List<Translation> Translations = this.translationService.listAllTranslations();
        assertNotNull(Translations);
        assertEquals(amountOfTranslations, Translations.size());
        assertEquals(firstTranslation.getShortDescription(), Translations.stream().
                min(Comparator.comparing(Translation::getShortDescription))
                .orElseThrow()
                .getShortDescription()
        );
    }

    /**
     * Create a random {@link Translation} for testing.
     *
     * @return A Translation with random values.
     */
    private Translation createTestTranslation() {
        return new Translation(
                UUID.randomUUID().toString().replace("-", "").substring(10),
                UUID.randomUUID().toString().replace("-", "")
        );
    }

}
