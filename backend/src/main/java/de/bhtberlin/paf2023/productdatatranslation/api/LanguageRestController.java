package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.LanguageDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageCrudService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for {@link Language} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/languages")
public class LanguageRestController {

    final LanguageCrudService languageCrudService;

    final ModelMapper mapper;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<LanguageDto>> listAllLanguages() {
        return ResponseEntity.ok(this.languageCrudService.listAllLanguages()
                .stream().map(language -> this.mapper.map(language, LanguageDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readLanguage(@PathVariable final int id) {
        Optional<Language> language = this.languageCrudService.readLanguage(id);
        if (language.isEmpty()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.NOT_FOUND,
                    "Language with Id " + id + " was not found."
            );
        }
        return ResponseEntity.ok(this.mapper.map(language.get(), LanguageDto.class));
    }

    /**
     * Create a new {@link Language} .
     * <p>
     * This method will enforce plain {@link Language} creation by removing all linked entities.
     */
    @PostMapping("")
    public ResponseEntity<LanguageDto> createLanguage(@RequestBody final LanguageDto dto) {
        dto.setTranslations(null);
        Language created = this.languageCrudService.createLanguage(this.mapper.map(dto, Language.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, LanguageDto.class));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> setLanguage(@PathVariable final int id, @RequestBody final LanguageDto dto) {
        if (id != dto.getId()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Mismatch between provided Ids."
            );
        }
        Language updated = this.languageCrudService.updateLanguage(this.mapper.map(dto, Language.class));
        return ResponseEntity.ok(this.mapper.map(updated, LanguageDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLanguage(@PathVariable final int id) {
        this.languageCrudService.deleteLanguageById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
