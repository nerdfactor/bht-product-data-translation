package de.bhtberlin.paf2023.productdatatranslation.crud;

import de.bhtberlin.paf2023.productdatatranslation.entity.Revision;
import de.bhtberlin.paf2023.productdatatranslation.service.RevisionCrudService;
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
 * Tests for basic crud operations of {@link Revision Revisions}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RevisionCrudIntegrationTests {

    @Autowired
    RevisionCrudService revisionService;

    /**
     * Check if a Revision can be read after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void RevisionCanBeCreatedAndRead() {
        Revision create = this.createTestRevision();
        Revision created = revisionService.createRevision(create);
        assertNotNull(created);
        assertNotEquals(0, created.getId());
        assertEquals(create.getShortDescription(), created.getShortDescription());

        Optional<Revision> read = this.revisionService.readRevision(created.getId());
        assertTrue(read.isPresent());
        assertEquals(read.get().getShortDescription(), create.getShortDescription());
    }

    /**
     * Check if a Revision can be updated after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void RevisionCanBeUpdated() {
        Revision create = this.createTestRevision();
        Revision created = revisionService.createRevision(create);
        assertNotNull(created);

        String updatedRevisionName = "Geänderter Name";
        created.setShortDescription(updatedRevisionName);
        Revision updated = this.revisionService.updateRevision(created);

        assertNotNull(updated);
        assertEquals(updatedRevisionName, updated.getShortDescription());
    }

    /**
     * Check if a Revision can't be read after it was deleted.
     */
    @Test
    @Transactional
    @Rollback
    public void RevisionCantBeReadAfterItWasDeleted() {
        Revision create1 = this.createTestRevision();
        Revision created1 = revisionService.createRevision(create1);
        assertNotNull(created1);

        this.revisionService.deleteRevision(created1);

        Optional<Revision> read1 = this.revisionService.readRevision(created1.getId());
        assertFalse(read1.isPresent());

        Revision create2 = this.createTestRevision();
        Revision created2 = revisionService.createRevision(create2);
        assertNotNull(created2);

        this.revisionService.deleteRevisionById(created2.getId());

        Optional<Revision> read2 = this.revisionService.readRevision(created2.getId());
        assertFalse(read2.isPresent());
    }

    /**
     * Checks if Revisions can be listed after they are created.
     */
    @Test
    @Transactional
    @Rollback
    public void RevisionsCanBeListed() {
        // todo: handle previously existing objects.
        int amountOfRevisions = 10;
        List<Revision> listOfTestRevisions = new ArrayList<>();
        for (int i = 0; i < amountOfRevisions; i++) {
            Revision create = this.createTestRevision();
            listOfTestRevisions.add(create);
            revisionService.createRevision(create);
        }
        Revision firstRevision = listOfTestRevisions.stream()
                .min(Comparator.comparing(Revision::getShortDescription))
                .orElse(this.createTestRevision());

        List<Revision> Revisions = this.revisionService.listAllRevisions();
        assertNotNull(Revisions);
        assertEquals(amountOfRevisions, Revisions.size());
        assertEquals(firstRevision.getShortDescription(), Revisions.stream().
                min(Comparator.comparing(Revision::getShortDescription))
                .orElseThrow()
                .getShortDescription()
        );
    }

    /**
     * Create a random {@link Revision} for testing.
     *
     * @return A Revision with random values.
     */
    private Revision createTestRevision() {
        return new Revision(
                1,
                System.currentTimeMillis(),
                UUID.randomUUID().toString().replace("-", "").substring(10),
                UUID.randomUUID().toString().replace("-", ""),
                false
        );
    }

}
