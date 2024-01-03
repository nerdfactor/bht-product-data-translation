package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.LanguageDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.TranslationDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotCreatedException;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageService;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslationCrudService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for {@link Translation} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/translations")
public class TranslationRestController {

    final TranslationCrudService translationCrudService;

    final LanguageService languageService;

    final ModelMapper mapper;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<TranslationDto>> listAllTranslations() {
        return ResponseEntity.ok(this.translationCrudService.listAllTranslations()
                .stream().map(translation -> this.mapper.map(translation, TranslationDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranslationDto> readTranslation(@PathVariable final int id) {
        Translation translation = this.translationCrudService.readTranslation(id).
                orElseThrow(() -> new EntityNotFoundException("Translation with Id " + id + " was not found."));
        return ResponseEntity.ok(this.mapper.map(translation, TranslationDto.class));
    }

    /**
     * Create a new {@link Translation}.
     */
    @PostMapping("")
    public ResponseEntity<TranslationDto> createTranslation(@RequestBody final TranslationDto dto) {
        // newly created translations will always be in the default language.
        dto.setLanguage(new LanguageDto(this.languageService.getDefaultLanguage().getId()));
        // newly created translations always require a product they are assigned to.
        if (dto.getProduct() == null) {
            throw new EntityNotCreatedException("Translation requires a Product to be created");
        }
        Translation created = this.translationCrudService.createTranslation(this.mapper.map(dto, Translation.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, TranslationDto.class));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<TranslationDto> setTranslation(@PathVariable final int id, @RequestBody final TranslationDto dto) {
        if (id != dto.getId()) {
            throw new UnprocessableEntityException(String.format("Mismatch between provided Ids (%d - %d).", id, dto.getId()));
        }
        Translation updated = this.translationCrudService.updateTranslation(this.mapper.map(dto, Translation.class));
        return ResponseEntity.ok(this.mapper.map(updated, TranslationDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTranslation(@PathVariable final int id) {
        this.translationCrudService.deleteTranslationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
