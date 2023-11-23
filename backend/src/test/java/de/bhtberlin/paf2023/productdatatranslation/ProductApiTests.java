package de.bhtberlin.paf2023.productdatatranslation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductApiTests {

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Check if a {@link Product} can be read after it was created using the REST endpoints.
     *
     * @throws Exception For Exceptions during mock mvc methods.
     */
    @Test
    @Transactional
    @Rollback
    public void productCanBeCreatedAndRead() throws Exception {
        ProductDto create = this.createTestProduct();
        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(create))
                )
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().string(containsString(create.getName())))
                .andReturn();
        ProductDto created = this.jsonMapper.readValue(result.getResponse().getContentAsString(), ProductDto.class);

        mockMvc.perform(get("/api/products/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(this.jsonMapper.writeValueAsString(created)))
                .andReturn();
    }

    /**
     * Create a random {@link ProductDto} for testing. Use Dto instead
     * of entity for Api Tests.
     *
     * @return A {@link ProductDto} with random values.
     */
    private ProductDto createTestProduct() {
        Random random = new Random();
        ProductDto dto = new ProductDto();
        dto.setSerial(UUID.randomUUID().toString());
        dto.setName(UUID.randomUUID().toString().replace("-", "").substring(10));
        dto.setHeight(1 + (100 - 1) * random.nextDouble());
        dto.setWidth(1 + (100 - 1) * random.nextDouble());
        dto.setDepth(1 + (100 - 1) * random.nextDouble());
        dto.setWeight(1 + (100 - 1) * random.nextDouble());
        dto.setPrice(1 + (100 - 1) * random.nextDouble());
        return dto;
    }
}
