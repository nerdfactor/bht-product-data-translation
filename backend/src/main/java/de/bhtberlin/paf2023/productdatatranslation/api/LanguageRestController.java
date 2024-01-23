package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.LanguageDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageCrudService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for {@link Language} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/languages")
public class LanguageRestController {

    /**
     * The {@link LanguageCrudService} for access to {@link Language Languages}.
     */
    final LanguageCrudService languageCrudService;

    /**
     * The {@link ModelMapper} used for mapping between Entity and DTOs.
     */
    final ModelMapper mapper;

    /**
     * List all {@link Language Languages}.
     *
     * @return A {@link ResponseEntity} containing a list of {@link LanguageDto} objects.
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<LanguageDto>> listAllLanguages() {
        return ResponseEntity.ok(this.languageCrudService.listAllLanguages()
                .stream().map(language -> this.mapper.map(language, LanguageDto.class))
                .toList());
    }

    /**
     * Read a single {@link Language} by its ID.
     *
     * @param id The ID of the {@link Language} to read.
     * @return A {@link ResponseEntity} containing a {@link LanguageDto} object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LanguageDto> readLanguage(@PathVariable final int id) {
        Language language = this.languageCrudService.readLanguage(id).
                orElseThrow(() -> new EntityNotFoundException("Language with Id " + id + " was not found."));
        return ResponseEntity.ok(this.mapper.map(language, LanguageDto.class));
    }

    /**
     * Create a new {@link Language}.
     *
     * @param dto The {@link LanguageDto} containing the data to create the {@link Language} from.
     * @return A {@link ResponseEntity} containing the created {@link LanguageDto}.
     */
    @PostMapping("")
    public ResponseEntity<LanguageDto> createLanguage(@RequestBody final LanguageDto dto) {
        Language created = this.languageCrudService.createLanguage(this.mapper.map(dto, Language.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, LanguageDto.class));
    }

    /**
     * Update an existing {@link Language}.
     *
     * @param id  The ID of the {@link Language} to update.
     * @param dto The {@link LanguageDto} containing the data to update the {@link Language} with.
     * @return A {@link ResponseEntity} containing the updated {@link LanguageDto}.
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<LanguageDto> setLanguage(@PathVariable final int id, @RequestBody final LanguageDto dto) {
        if (id != dto.getId()) {
            throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
        }
        Language updated = this.languageCrudService.updateLanguage(this.mapper.map(dto, Language.class));
        return ResponseEntity.ok(this.mapper.map(updated, LanguageDto.class));
    }

    /**
     * Delete an existing {@link Language}.
     *
     * @param id The ID of the {@link Language} to delete.
     * @return A {@link ResponseEntity} with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable final int id) {
        this.languageCrudService.deleteLanguageById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
