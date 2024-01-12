package de.bhtberlin.paf2023.productdatatranslation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TranslationControllerTest {

    private static final String API_PATH = "/api/translations";

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    Translator translator;

    /**
     * Check if a set of internationalization data can be auto translated.
     */
    @Test
    void shouldTranslateI18nData() throws Exception {
        Map<String, String> i18n = new HashMap<>();
        i18n.put("test", "some testable string");
        mockMvc.perform(post(API_PATH + "/i18n")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(i18n))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.test").value(this.translator.translateText(i18n.get("test"), AppConfig.DEFAULT_LANGUAGE, "en")));
    }

}
