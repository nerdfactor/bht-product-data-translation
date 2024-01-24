package de.bhtberlin.paf2023.productdatatranslation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.LanguageDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.service.LanguageService;
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
 * Test for {@link Language} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class LanguageControllerTest {

    private static final String API_PATH = "/api/languages";

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked {@link LanguageService} in order to provide
     * mock responses to the tested REST controller.
     */
    @MockBean
    LanguageService languageService;

    /**
     * Check if {@link Language Languages} can be listed.
     */
    @Test
    void languagesCanBeListed() throws Exception {
        List<LanguageDto> mockDtos = Arrays.asList(
                createTestLanguage(1),
                createTestLanguage(2)
        );
        List<Language> mockEntities = mockDtos.stream().map(dto -> this.modelMapper.map(dto, Language.class)).toList();
        Mockito.when(languageService.listAllLanguages())
                .thenReturn(mockEntities);

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDtos)));
    }

    /**
     * Check if a {@link Language} can be created.
     */
    @Test
    void languageCanBeCreated() throws Exception {
        LanguageDto mockDto = createTestLanguage();
        Mockito.when(languageService.createLanguage(any(Language.class)))
                .thenReturn(this.modelMapper.map(mockDto, Language.class));

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(new Language()))
                ).andExpect(status().isCreated())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Language} can be read.
     */
    @Test
    void languageCanBeRead() throws Exception {
        LanguageDto mockDto = createTestLanguage();
        Mockito.when(languageService.readLanguage(any(int.class)))
                .thenReturn(Optional.of(this.modelMapper.map(mockDto, Language.class)));

        mockMvc.perform(get(API_PATH + "/" + mockDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Language} can be updated.
     */
    @Test
    void languageCanBeUpdated() throws Exception {
        LanguageDto mockDto = createTestLanguage(1);
        Mockito.when(languageService.updateLanguage(argThat(argument -> argument.getId() == mockDto.getId())))
                .thenReturn(this.modelMapper.map(mockDto, Language.class));

        mockMvc.perform(put(API_PATH + "/" + mockDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(mockDto))
                ).andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Language} can be deleted.
     */
    @Test
    void languageCanBeDeleted() throws Exception {
        mockMvc.perform(delete(API_PATH + "/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    private LanguageDto createTestLanguage() {
        LanguageDto dto = new LanguageDto();
        dto.setName(UUID.randomUUID().toString().replace("-", "").substring(10));
        dto.setIsoCode(dto.getName().substring(0, 2));
        return dto;
    }

    private LanguageDto createTestLanguage(int id) {
        LanguageDto dto = createTestLanguage();
        dto.setId(id);
        return dto;
    }
}
