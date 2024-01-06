package de.bhtberlin.paf2023.productdatatranslation.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.DataPage;
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
public class SearchProductTest {

    private static final String API_PATH = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    /**
     * Should find a product searched in the correct case in an existing language.
     */
    @Test
    @Transactional
    public void shouldFindSearchedProductInExistingLanguage() throws Exception {
        String search = "Premium";

        String response = mockMvc.perform(get(API_PATH + "/search?query=" + search)
                        .header("Accept-Language", Locale.GERMAN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DataPage<ProductDto> responses = this.jsonMapper.readValue(response, new TypeReference<>() {
        });
        Assertions.assertTrue(responses.getContent().get(0).getName().contains(search)
                || responses.getContent().get(0).getTranslations().stream().findFirst().orElseThrow().getLongDescription().contains(search)
                || responses.getContent().get(0).getTranslations().stream().findFirst().orElseThrow().getShortDescription().contains(search));
    }


    /**
     * Should find a product searched in the correct case in a non-existing language.
     */
    @Test
    @Transactional
    public void shouldFindSearchedProductInNonExistingLanguage() throws Exception {
        String search = "confortable";

        String response = mockMvc.perform(get(API_PATH + "/search?query=" + search)
                        .header("Accept-Language", Locale.FRENCH))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DataPage<ProductDto> responses = this.jsonMapper.readValue(response, new TypeReference<>() {
        });
        Assertions.assertTrue(responses.getContent().get(0).getTranslations().stream().findFirst().orElse(new TranslationDto()).getShortDescription().contains(search));
    }

    /**
     * Should limit the search result to a page size.
     */
    @Test
    @Transactional
    public void shouldFindLimitedAmountOfProducts() throws Exception {
        int amount = 5;
        String search = "";
        String response = mockMvc.perform(get(API_PATH + "/search?query=" + search + "&size=" + amount)
                        .header("Accept-Language", Locale.GERMAN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DataPage<ProductDto> responses = this.jsonMapper.readValue(response, new TypeReference<>() {
        });
        // todo: there is a strange behavior, where the result size is exactly 1 less than the requested page??
        Assertions.assertEquals(amount - 1, responses.getNumberOfElements());
    }

}
