package de.bhtberlin.paf2023.productdatatranslation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Currency;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.entity.Measurement;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.CurrencyConversionStrategy;
import de.bhtberlin.paf2023.productdatatranslation.translation.strategy.FakeCurrencyConversionStrategy;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test for {@link Product} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductRestControllerTest {

    private static final String API_PATH = "/api/products";

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked {@link ProductCrudService} in order to provide
     * mock responses to the tested REST controller.
     */
    @MockBean
    ProductCrudService productCrudService;

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
     * Check if {@link Product Products} can be listed.
     */
    @Test
    void productsCanBeListed() throws Exception {
        List<ProductDto> mockDtos = Arrays.asList(
                createTestProduct(1),
                createTestProduct(2)
        );
        List<Product> mockEntities = mockDtos.stream().map(dto -> this.modelMapper.map(dto, Product.class)).toList();
        Mockito.when(productCrudService.listAllProducts(any(Locale.class)))
                .thenReturn(mockEntities);

        mockDtos.forEach(dto -> dto.translate(translator, this.de, this.en));
        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDtos)));
    }

    /**
     * Check if a {@link Product} can be created.
     */
    @Test
    void productCanBeCreated() throws Exception {
        ProductDto mockDto = createTestProduct();
        Mockito.when(productCrudService.createProduct(any(Product.class)))
                .thenReturn(this.modelMapper.map(mockDto, Product.class));

        mockDto.translate(translator, this.de, this.en);
        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(new Product()))
                ).andExpect(status().isCreated())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Product} can be read.
     */
    @Test
    void productCanBeRead() throws Exception {
        ProductDto mockDto = createTestProduct(1);
        Mockito.when(productCrudService.readProduct(any(int.class), any(Locale.class)))
                .thenReturn(Optional.of(this.modelMapper.map(mockDto, Product.class)));

        mockDto.translate(translator, this.de, this.en);
        mockMvc.perform(get(API_PATH + "/" + mockDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Product} can be updated.
     */
    @Test
    void productCanBeUpdated() throws Exception {
        ProductDto mockDto = createTestProduct(1);
        Mockito.when(productCrudService.updateProduct(argThat(argument -> argument.getId() == mockDto.getId())))
                .thenReturn(this.modelMapper.map(mockDto, Product.class));

        mockDto.translate(translator, this.de, this.en);
        mockMvc.perform(put(API_PATH + "/" + mockDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(mockDto))
                ).andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Product} can be deleted.
     */
    @Test
    void productCanBeDeleted() throws Exception {
        mockMvc.perform(delete(API_PATH + "/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

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

    private ProductDto createTestProduct(int id) {
        ProductDto dto = createTestProduct();
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
