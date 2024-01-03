package de.bhtberlin.paf2023.productdatatranslation.crud;

import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.service.ColorCrudService;
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
 * Tests for basic crud operations of {@link Color Colors}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ColorCrudIntegrationTests {

    @Autowired
    ColorCrudService colorService;

    /**
     * Check if a Color can be read after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void colorCanBeCreatedAndRead() {
        Color create = this.createTestColor();
        Color created = colorService.createColor(create);
        assertNotNull(created);
        assertNotEquals(0, created.getId());
        assertEquals(create.getName(), created.getName());

        Optional<Color> read = this.colorService.readColor(created.getId());
        assertTrue(read.isPresent());
        assertEquals(read.get().getName(), create.getName());
    }

    /**
     * Check if a Color can be updated after it was created.
     */
    @Test
    @Transactional
    @Rollback
    public void colorCanBeUpdated() {
        Color create = this.createTestColor();
        Color created = colorService.createColor(create);
        assertNotNull(created);

        String updatedColorName = "Geänderter Name";
        created.setName(updatedColorName);
        Color updated = this.colorService.updateColor(created);

        assertNotNull(updated);
        assertEquals(updatedColorName, updated.getName());
    }

    /**
     * Check if a Color can't be read after it was deleted.
     */
    @Test
    @Transactional
    @Rollback
    public void colorCantBeReadAfterItWasDeleted() {
        Color create1 = this.createTestColor();
        Color created1 = colorService.createColor(create1);
        assertNotNull(created1);

        this.colorService.deleteColor(created1);

        Optional<Color> read1 = this.colorService.readColor(created1.getId());
        assertFalse(read1.isPresent());

        Color create2 = this.createTestColor();
        Color created2 = colorService.createColor(create2);
        assertNotNull(created2);

        this.colorService.deleteColorById(created2.getId());

        Optional<Color> read2 = this.colorService.readColor(created2.getId());
        assertFalse(read2.isPresent());
    }

    /**
     * Checks if Colors can be listed after they are created.
     */
    @Test
    @Transactional
    @Rollback
    public void colorsCanBeListed() {
        int previousAmountOfColors = this.colorService.listAllColors().size();
        int amountOfColors = 10;
        List<Color> listOfTestColors = new ArrayList<>();
        for (int i = 0; i < amountOfColors; i++) {
            Color create = this.createTestColor();
            listOfTestColors.add(create);
            colorService.createColor(create);
        }
        Color firstColor = listOfTestColors.stream()
                .min(Comparator.comparing(Color::getName))
                .orElse(this.createTestColor());

        List<Color> colors = this.colorService.listAllColors();
        assertNotNull(colors);
        assertEquals(amountOfColors+previousAmountOfColors, colors.size());
        assertEquals(firstColor.getName(), colors.stream().
                min(Comparator.comparing(Color::getName))
                .orElseThrow()
                .getName()
        );
    }

    /**
     * Create a random {@link Color} for testing.
     *
     * @return A Color with random values.
     */
    private Color createTestColor() {
        return new Color(
                UUID.randomUUID().toString().replace("-", "").substring(10)
        );
    }

}
