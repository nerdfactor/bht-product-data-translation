package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.ColorCrudService;
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
	public ResponseEntity<ColorDto> readColor(@PathVariable final int id) {
		Color color = this.colorCrudService.readColor(id).
				orElseThrow(() -> new EntityNotFoundException("Color with Id " + id + " was not found."));
		return ResponseEntity.ok(this.mapper.map(color, ColorDto.class));
	}

	/**
	 * Create a new {@link Color} .
	 * <p>
	 * This method will enforce plain {@link Color} creation by removing all linked entities.
	 */
	@PostMapping("")
	public ResponseEntity<ColorDto> createColor(@RequestBody final ColorDto dto) {
		Color created = this.colorCrudService.createColor(this.mapper.map(dto, Color.class));
		return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, ColorDto.class));
	}

	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
	public ResponseEntity<ColorDto> setColor(@PathVariable final int id, @RequestBody final ColorDto dto) {
		if (id != dto.getId()) {
			throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
		}
		Color updated = this.colorCrudService.updateColor(this.mapper.map(dto, Color.class));
		return ResponseEntity.ok(this.mapper.map(updated, ColorDto.class));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteColor(@PathVariable final int id) {
		this.colorCrudService.deleteColorById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
