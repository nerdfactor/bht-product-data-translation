package de.bhtberlin.paf2023.productdatatranslation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.TranslationDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageService;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test for {@link Translation} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TranslationRestControllerTest {

    private static final String API_PATH = "/api/translations";

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked {@link TranslationService} in order to provide
     * mock responses to the tested REST controller.
     */
    @MockBean
    TranslationService translationService;

    @MockBean
    LanguageService languageService;

    /**
     * Check if {@link Translation Translations} can be listed.
     */
    @Test
    void translationsCanBeListed() throws Exception {
        List<TranslationDto> mockDtos = Arrays.asList(
                createTestTranslation(1),
                createTestTranslation(2)
        );
        List<Translation> mockEntities = mockDtos.stream().map(dto -> this.modelMapper.map(dto, Translation.class)).toList();
        Mockito.when(translationService.listAllTranslations())
                .thenReturn(mockEntities);

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDtos)));
    }

    /**
     * Check if a {@link Translation} can be created.
     */
    @Test
    void translationCanBeCreated() throws Exception {
        TranslationDto mockDto = createTestTranslation();
        mockDto.setProduct(new ProductDto(1));
        Mockito.when(translationService.createTranslation(any(Translation.class)))
                .thenReturn(this.modelMapper.map(mockDto, Translation.class));
        Mockito.when(languageService.getDefaultLanguage())
                .thenReturn(new Language());

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(mockDto))
                ).andExpect(status().isCreated())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Translation} can be read.
     */
    @Test
    void translationCanBeRead() throws Exception {
        TranslationDto mockDto = createTestTranslation();
        Mockito.when(translationService.readTranslation(any(int.class)))
                .thenReturn(Optional.of(this.modelMapper.map(mockDto, Translation.class)));

        mockMvc.perform(get(API_PATH + "/" + mockDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Translation} can be updated.
     */
    @Test
    void translationCanBeUpdated() throws Exception {
        TranslationDto mockDto = createTestTranslation(1);
        Mockito.when(translationService.updateTranslation(argThat(argument -> argument.getId() == mockDto.getId())))
                .thenReturn(this.modelMapper.map(mockDto, Translation.class));

        mockMvc.perform(put(API_PATH + "/" + mockDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(mockDto))
                ).andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Translation} can be deleted.
     */
    @Test
    void translationCanBeDeleted() throws Exception {
        mockMvc.perform(delete(API_PATH + "/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    private TranslationDto createTestTranslation() {
        String description = UUID.randomUUID().toString() + UUID.randomUUID() + UUID.randomUUID();
        TranslationDto dto = new TranslationDto();
        dto.setShortDescription(description.substring(0, 20));
        dto.setLongDescription(description);
        return dto;
    }

    private TranslationDto createTestTranslation(int id) {
        TranslationDto dto = createTestTranslation();
        dto.setId(id);
        return dto;
    }
}
