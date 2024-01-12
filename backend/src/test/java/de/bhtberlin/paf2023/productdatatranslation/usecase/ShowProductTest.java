package de.bhtberlin.paf2023.productdatatranslation.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.TranslationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class ShowProductTest {

    private static final String API_PATH = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    /**
     * Should return product details in an existing language.
     */
    @Test
    @Transactional
    void shouldReturnProductInExistingLanguage() throws Exception {
        int id = 1;
        String response = mockMvc.perform(get(API_PATH + "/" + id)
                        .header("Accept-Language", Locale.GERMAN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProductDto responseDto = this.jsonMapper.readValue(response, ProductDto.class);
        Assertions.assertEquals(id, responseDto.getId());
    }

    /**
     * Should return product details in a non-existing language.
     */
    @Test
    @Transactional
    void shouldReturnProductInNonExistingLanguage() throws Exception {
        int id = 1;
        String response = mockMvc.perform(get(API_PATH + "/" + id)
                        .header("Accept-Language", Locale.FRENCH))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProductDto responseDto = this.jsonMapper.readValue(response, ProductDto.class);
        Assertions.assertEquals(id, responseDto.getId());
        Assertions.assertTrue(responseDto.getTranslations().size() > 1);
        boolean hasCorrectTranslation = false;
        for (TranslationDto translation : responseDto.getTranslations()) {
            if (translation.getLanguage().getIsoCode().equalsIgnoreCase(Locale.FRENCH.toLanguageTag())) {
                hasCorrectTranslation = true;
            }
        }
        Assertions.assertTrue(hasCorrectTranslation);
    }
}
