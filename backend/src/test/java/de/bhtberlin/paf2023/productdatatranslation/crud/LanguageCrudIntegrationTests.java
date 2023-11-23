package de.bhtberlin.paf2023.productdatatranslation.crud;

import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageCrudService;
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
 * Tests for basic crud operations of {@link Language Languages}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LanguageCrudIntegrationTests {

    @Autowired
    LanguageCrudService languageService;

    /**
     * Check if a Language can be read after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void LanguageCanBeCreatedAndRead() {
        Language create = this.createTestLanguage();
        Language created = languageService.createLanguage(create);
        assertNotNull(created);
        assertNotEquals(0, created.getId());
        assertEquals(create.getName(), created.getName());

        Optional<Language> read = this.languageService.readLanguage(created.getId());
        assertTrue(read.isPresent());
        assertEquals(read.get().getName(), create.getName());
    }

    /**
     * Check if a Language can be updated after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void LanguageCanBeUpdated() {
        Language create = this.createTestLanguage();
        Language created = languageService.createLanguage(create);
        assertNotNull(created);

        String updatedLanguageName = "Geänderter Name";
        created.setName(updatedLanguageName);
        Language updated = this.languageService.updateLanguage(created);

        assertNotNull(updated);
        assertEquals(updatedLanguageName, updated.getName());
    }

    /**
     * Check if a Language can't be read after it was deleted.
     */
    @Test
    @Transactional
    @Rollback
    public void LanguageCantBeReadAfterItWasDeleted() {
        Language create1 = this.createTestLanguage();
        Language created1 = languageService.createLanguage(create1);
        assertNotNull(created1);

        this.languageService.deleteLanguage(created1);

        Optional<Language> read1 = this.languageService.readLanguage(created1.getId());
        assertFalse(read1.isPresent());

        Language create2 = this.createTestLanguage();
        Language created2 = languageService.createLanguage(create2);
        assertNotNull(created2);

        this.languageService.deleteLanguageById(created2.getId());

        Optional<Language> read2 = this.languageService.readLanguage(created2.getId());
        assertFalse(read2.isPresent());
    }

    /**
     * Checks if Languages can be listed after they are created.
     */
    @Test
    @Transactional
    @Rollback
    public void LanguagesCanBeListed() {
        // todo: handle previously existing objects.
        int amountOfLanguages = 10;
        List<Language> listOfTestLanguages = new ArrayList<>();
        for (int i = 0; i < amountOfLanguages; i++) {
            Language create = this.createTestLanguage();
            listOfTestLanguages.add(create);
            languageService.createLanguage(create);
        }
        Language firstLanguage = listOfTestLanguages.stream()
                .min(Comparator.comparing(Language::getName))
                .orElse(this.createTestLanguage());

        List<Language> Languages = this.languageService.listAllLanguages();
        assertNotNull(Languages);
        assertEquals(amountOfLanguages, Languages.size());
        assertEquals(firstLanguage.getName(), Languages.stream().
                min(Comparator.comparing(Language::getName))
                .orElseThrow()
                .getName()
        );
    }

    /**
     * Create a random {@link Language} for testing.
     *
     * @return A Language with random values.
     */
    private Language createTestLanguage() {
        return new Language(
                UUID.randomUUID().toString().replace("-", "").substring(10),
                "euro",
                "metric",
                "de_DE"
        );
    }

}
