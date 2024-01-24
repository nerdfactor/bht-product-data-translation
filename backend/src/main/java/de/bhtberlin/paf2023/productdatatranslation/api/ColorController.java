package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for {@link Color} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/colors")
public class ColorController {

    /**
     * The {@link ColorService} for access to {@link Color Colors}.
     */
    final ColorService colorService;

    /**
     * The {@link ModelMapper} used for mapping between Entity and DTOs.
     */
    final ModelMapper mapper;

    /**
     * List all {@link Color Colors}.
     *
     * @return A {@link ResponseEntity} containing a list of {@link ColorDto} objects.
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<ColorDto>> listAllColors() {
        return ResponseEntity.ok(this.colorService.listAllColors()
                .stream().map(color -> this.mapper.map(color, ColorDto.class))
                .toList());
    }

    /**
     * Read a single {@link Color} by its ID.
     *
     * @param id The ID of the {@link Color} to read.
     * @return A {@link ResponseEntity} containing a {@link ColorDto} object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColorDto> readColor(@PathVariable final int id) {
        Color color = this.colorService.readColor(id).
                orElseThrow(() -> new EntityNotFoundException("Color with Id " + id + " was not found."));
        return ResponseEntity.ok(this.mapper.map(color, ColorDto.class));
    }

    /**
     * Create a new {@link Color}.
     *
     * @param dto The {@link ColorDto} containing the data for the new {@link Color}.
     * @return A {@link ResponseEntity} containing the created {@link ColorDto}.
     */
    @PostMapping("")
    public ResponseEntity<ColorDto> createColor(@RequestBody final ColorDto dto) {
        Color created = this.colorService.createColor(this.mapper.map(dto, Color.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, ColorDto.class));
    }

    /**
     * Update an existing {@link Color}.
     *
     * @param id  The ID of the {@link Color} to update.
     * @param dto The {@link ColorDto} containing the data for the updated {@link Color}.
     * @return A {@link ResponseEntity} containing the updated {@link ColorDto}.
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<ColorDto> setColor(@PathVariable final int id, @RequestBody final ColorDto dto) {
        if (id != dto.getId()) {
            throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
        }
        Color updated = this.colorService.updateColor(this.mapper.map(dto, Color.class));
        return ResponseEntity.ok(this.mapper.map(updated, ColorDto.class));
    }

    /**
     * Delete an existing {@link Color}.
     *
     * @param id The ID of the {@link Color} to delete.
     * @return A {@link ResponseEntity} with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable final int id) {
        this.colorService.deleteColorById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
