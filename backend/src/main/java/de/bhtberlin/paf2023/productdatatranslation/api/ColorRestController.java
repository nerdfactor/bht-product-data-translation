package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.service.ColorCrudService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for {@link Color} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/colors")
public class ColorRestController {

    final ColorCrudService colorCrudService;

    final ModelMapper mapper;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<ColorDto>> listAllColors() {
        return ResponseEntity.ok(this.colorCrudService.listAllColors()
                .stream().map(color -> this.mapper.map(color, ColorDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readColor(@PathVariable final int id) {
        Optional<Color> color = this.colorCrudService.readColor(id);
        if (color.isEmpty()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.NOT_FOUND,
                    "Color with Id " + id + " was not found."
            );
        }
        return ResponseEntity.ok(this.mapper.map(color.get(), ColorDto.class));
    }

    /**
     * Create a new {@link Color} .
     * <p>
     * This method will enforce plain {@link Color} creation by removing all linked entities.
     */
    @PostMapping("")
    public ResponseEntity<ColorDto> createColor(@RequestBody final ColorDto dto) {
        dto.setProducts(null);
        Color created = this.colorCrudService.createColor(this.mapper.map(dto, Color.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, ColorDto.class));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> setColor(@PathVariable final int id, @RequestBody final ColorDto dto) {
        if (id != dto.getId()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Mismatch between provided Ids."
            );
        }
        Color updated = this.colorCrudService.updateColor(this.mapper.map(dto, Color.class));
        return ResponseEntity.ok(this.mapper.map(updated, ColorDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable final int id) {
        this.colorCrudService.deleteColorById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
