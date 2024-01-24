package de.bhtberlin.paf2023.productdatatranslation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.entity.Currency;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Measurement;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import de.bhtberlin.paf2023.productdatatranslation.service.ColorService;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import org.junit.jupiter.api.BeforeEach;
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
 * Test for {@link Color} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ColorControllerTest {

    private static final String API_PATH = "/api/colors";

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked {@link ColorService} in order to provide
     * mock responses to the tested REST controller.
     */
    @MockBean
    ColorService colorService;

    @MockBean
    LanguageRepository languageRepository;

    @Autowired
    Translator translator;

    Language de = createTestLanguage("de", "EUR", "kg", "cm");
    Language en = createTestLanguage("en", "USD", "lb", "in");

    @BeforeEach
    public void setup() {
        Mockito.when(languageRepository.findOneByIsoCode("de"))
                .thenReturn(Optional.of(this.de));
        Mockito.when(languageRepository.findOneByIsoCode("en"))
                .thenReturn(Optional.of(this.en));
    }

    /**
     * Check if {@link Color Colors} can be listed.
     */
    @Test
    void colorsCanBeListed() throws Exception {
        List<ColorDto> mockDtos = Arrays.asList(
                createTestColor(1),
                createTestColor(2)
        );
        List<Color> mockEntities = mockDtos.stream().map(dto -> this.modelMapper.map(dto, Color.class)).toList();
        Mockito.when(colorService.listAllColors())
                .thenReturn(mockEntities);

        mockDtos.forEach(dto -> dto.translate(translator, this.de, this.en));
        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDtos)));
    }

    /**
     * Check if a {@link Color} can be created.
     */
    @Test
    void colorCanBeCreated() throws Exception {
        ColorDto mockDto = createTestColor();
        Mockito.when(colorService.createColor(any(Color.class)))
                .thenReturn(this.modelMapper.map(mockDto, Color.class));

        mockDto.translate(translator, this.de, this.en);
        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(new Color()))
                ).andExpect(status().isCreated())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Color} can be read.
     */
    @Test
    void colorCanBeRead() throws Exception {
        ColorDto mockDto = createTestColor();
        Mockito.when(colorService.readColor(any(int.class)))
                .thenReturn(Optional.of(this.modelMapper.map(mockDto, Color.class)));

        mockDto.translate(translator, this.de, this.en);
        mockMvc.perform(get(API_PATH + "/" + mockDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Color} can be updated.
     */
    @Test
    void colorCanBeUpdated() throws Exception {
        ColorDto mockDto = createTestColor(1);
        Mockito.when(colorService.updateColor(argThat(argument -> argument.getId() == mockDto.getId())))
                .thenReturn(this.modelMapper.map(mockDto, Color.class));

        mockDto.translate(translator, this.de, this.en);
        mockMvc.perform(put(API_PATH + "/" + mockDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(mockDto))
                ).andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Color} can be deleted.
     */
    @Test
    void colorCanBeDeleted() throws Exception {
        mockMvc.perform(delete(API_PATH + "/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    private ColorDto createTestColor() {
        ColorDto dto = new ColorDto();
        dto.setName(UUID.randomUUID().toString().replace("-", "").substring(10));
        return dto;
    }

    private ColorDto createTestColor(int id) {
        ColorDto dto = createTestColor();
        dto.setId(id);
        return dto;
    }

    private Language createTestLanguage(String lang, String cur, String weight, String distance) {
        Language language = new Language();
        language.setIsoCode(lang);
        Currency currency = new Currency();
        currency.setIsoCode(cur);
        language.setCurrency(currency);
        Measurement measurement = new Measurement();
        measurement.setWeight(weight);
        measurement.setDepth(distance);
        measurement.setHeight(distance);
        measurement.setWidth(distance);
        language.setMeasurement(measurement);
        return language;
    }
}
