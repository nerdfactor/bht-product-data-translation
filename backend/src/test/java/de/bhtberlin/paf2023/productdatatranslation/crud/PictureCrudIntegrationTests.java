package de.bhtberlin.paf2023.productdatatranslation.crud;

import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.service.PictureService;
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
 * Tests for basic crud operations of {@link Picture Pictures}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureCrudIntegrationTests {

    @Autowired
    PictureService pictureService;

    /**
     * Check if a Picture can be read after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void PictureCanBeCreatedAndRead() {
        Picture create = this.createTestPicture();
        Picture created = pictureService.createPicture(create);
        assertNotNull(created);
        assertNotEquals(0, created.getId());
        assertEquals(create.getFilename(), created.getFilename());

        Optional<Picture> read = this.pictureService.readPicture(created.getId());
        assertTrue(read.isPresent());
        assertEquals(read.get().getFilename(), create.getFilename());
    }

    /**
     * Check if a Picture can be updated after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void PictureCanBeUpdated() {
        Picture create = this.createTestPicture();
        Picture created = pictureService.createPicture(create);
        assertNotNull(created);

        String updatedPictureName = "Geänderter Name";
        created.setFilename(updatedPictureName);
        Picture updated = this.pictureService.updatePicture(created);

        assertNotNull(updated);
        assertEquals(updatedPictureName, updated.getFilename());
    }

    /**
     * Check if a Picture can't be read after it was deleted.
     */
    @Test
    @Transactional
    @Rollback
    public void PictureCantBeReadAfterItWasDeleted() {
        Picture create1 = this.createTestPicture();
        Picture created1 = pictureService.createPicture(create1);
        assertNotNull(created1);

        this.pictureService.deletePicture(created1);

        Optional<Picture> read1 = this.pictureService.readPicture(created1.getId());
        assertFalse(read1.isPresent());

        Picture create2 = this.createTestPicture();
        Picture created2 = pictureService.createPicture(create2);
        assertNotNull(created2);

        this.pictureService.deletePictureById(created2.getId());

        Optional<Picture> read2 = this.pictureService.readPicture(created2.getId());
        assertFalse(read2.isPresent());
    }

    /**
     * Checks if Pictures can be listed after they are created.
     */
    @Test
    @Transactional
    @Rollback
    public void PicturesCanBeListed() {
        // todo: handle previously existing objects.
        int amountOfPictures = 10;
        List<Picture> listOfTestPictures = new ArrayList<>();
        for (int i = 0; i < amountOfPictures; i++) {
            Picture create = this.createTestPicture();
            listOfTestPictures.add(create);
            pictureService.createPicture(create);
        }
        Picture firstPicture = listOfTestPictures.stream()
                .min(Comparator.comparing(Picture::getFilename))
                .orElse(this.createTestPicture());

        List<Picture> Pictures = this.pictureService.listAllPictures();
        assertNotNull(Pictures);
        assertEquals(amountOfPictures, Pictures.size());
        assertEquals(firstPicture.getFilename(), Pictures.stream().
                min(Comparator.comparing(Picture::getFilename))
                .orElseThrow()
                .getFilename()
        );
    }

    /**
     * Create a random {@link Picture} for testing.
     *
     * @return A Picture with random values.
     */
    private Picture createTestPicture() {
        Random random = new Random();
        return new Picture(
                UUID.randomUUID().toString().replace("-", "").substring(10),
                "jpeg",
                1 + (100 - 1) * random.nextInt(),
                1 + (100 - 1) * random.nextInt()
        );
    }

}
