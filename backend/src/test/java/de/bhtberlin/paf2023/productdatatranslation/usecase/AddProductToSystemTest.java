package de.bhtberlin.paf2023.productdatatranslation.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.TranslationDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Category;
import de.bhtberlin.paf2023.productdatatranslation.entity.Color;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.entity.Translation;
import de.bhtberlin.paf2023.productdatatranslation.service.CategoryCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.ColorCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslationCrudService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class AddProductToSystemTest {

    private static final String API_PATH_PRODUCTS = "/api/products";

    private static final String API_PATH_TRANSLATIONS = "/api/translations";

    private static final String API_PATH_CATEGORIES = "/api/categories";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductCrudService productService;

    @Autowired
    TranslationCrudService translationService;

    @Autowired
    CategoryCrudService categoryService;

    @Autowired
    ColorCrudService colorService;

    /**
     * Should add a simple product into the system.
     */
    @Test
    @Transactional
    @Rollback
    void shouldAddSimpleProductToSystem() throws Exception {
        Product product = createTestProduct();
        ProductDto dto = this.modelMapper.map(product, ProductDto.class);

        String response = mockMvc.perform(post(API_PATH_PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(dto))
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        ProductDto responseDto = this.jsonMapper.readValue(response, ProductDto.class);
        Assertions.assertEquals(dto.getName(), responseDto.getName());
    }

    /**
     * Should add a product with existing color and category to the system.
     */
    @Test
    @Transactional
    @Rollback
    void shouldAddProductWithExistingColorAndCategoryToSystem() throws Exception {
        // load existing color and category
        Color color = this.colorService.readColor(1).orElseThrow();
        Category category = this.categoryService.readCategory(1).orElseThrow();

        // create a test product with those color and category
        Product product = createTestProduct();
        product.addColor(color);
        product.addCategory(category);

        // create a dto to send from the product
        ProductDto dto = this.modelMapper.map(product, ProductDto.class);

        // create the product. this should throw away the color and category.
        String responseCreated = mockMvc.perform(post(API_PATH_PRODUCTS)
                        .header("Accept-Language", "de")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(dto))
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        ProductDto responseCreatedDto = this.jsonMapper.readValue(responseCreated, ProductDto.class);
        Assertions.assertEquals(dto.getName(), responseCreatedDto.getName());

        // update the relation to color and category separately
        dto.setId(responseCreatedDto.getId());
        String responseUpdated = mockMvc.perform(patch(API_PATH_PRODUCTS + "/" + responseCreatedDto.getId())
                        .header("Accept-Language", "de")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(dto))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ProductDto responseUpdatedDto = this.jsonMapper.readValue(responseUpdated, ProductDto.class);
        Assertions.assertEquals(dto.getName(), responseUpdatedDto.getName());
        Assertions.assertEquals(1, responseUpdatedDto.getCategories().size());
        Assertions.assertEquals(1, responseUpdatedDto.getColors().size());
    }

    /**
     * Should add a product with new category to the system.
     */
    @Test
    @Transactional
    @Rollback
    void shouldAddProductWithNewCategoryToSystem() throws Exception {
        // create a test product
        Product product = createTestProduct();

        // create a dto to send from the product
        ProductDto dto = this.modelMapper.map(product, ProductDto.class);

        // create the product. this should throw away the color and category.
        String responseCreated = mockMvc.perform(post(API_PATH_PRODUCTS)
                        .header("Accept-Language", "de")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(dto))
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        ProductDto responseCreatedDto = this.jsonMapper.readValue(responseCreated, ProductDto.class);
        Assertions.assertEquals(dto.getName(), responseCreatedDto.getName());


        // create the new category
        Category category = createTestCategory();
        CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);

        String responseCategory = mockMvc.perform(post(API_PATH_CATEGORIES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(categoryDto))
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CategoryDto responseCategoryDto = this.jsonMapper.readValue(responseCategory, CategoryDto.class);
        Assertions.assertEquals(categoryDto.getName(), responseCategoryDto.getName());


        // update the relation to category
        ProductDto updateDto = this.modelMapper.map(product, ProductDto.class);
        updateDto.setId(responseCreatedDto.getId());
        updateDto.setCategories(Set.of(new CategoryDto(responseCategoryDto.getId())));
        String responseUpdated = mockMvc.perform(patch(API_PATH_PRODUCTS + "/" + responseCreatedDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(updateDto))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ProductDto responseUpdatedDto = this.jsonMapper.readValue(responseUpdated, ProductDto.class);
        Assertions.assertEquals(responseCreatedDto.getName(), responseUpdatedDto.getName());
        Assertions.assertEquals(1, responseUpdatedDto.getCategories().size());
    }

    /**
     * Should add a product with a new description to the system.
     */
    @Test
    @Transactional
    @Rollback
    void shouldAddProductWithDescriptionToSystem() throws Exception {
        // create a test product with those color and category
        Product product = createTestProduct();

        // create a dto to send from the product
        ProductDto productDto = this.modelMapper.map(product, ProductDto.class);

        // create the product. this should throw away the color and category.
        String responseCreated = mockMvc.perform(post(API_PATH_PRODUCTS)
                        .header("Accept-Language", "de")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(productDto))
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        ProductDto responseCreatedDto = this.jsonMapper.readValue(responseCreated, ProductDto.class);
        Assertions.assertEquals(productDto.getName(), responseCreatedDto.getName());


        // prepare by loading the existing translations and calculate the next id
        List<Translation> translationList = this.translationService.listAllTranslations().stream()
                .sorted(Comparator.comparingInt(Translation::getId))
                .toList();
        int expectedNextTranslationId = translationList.get(translationList.size() - 1).getId() + 1;

        // create the description as a translation in the default language
        Translation translation = createTestTranslation();

        // create a dto to send from the translation
        TranslationDto translationDto = this.modelMapper.map(translation, TranslationDto.class);
        translationDto.setProduct(new ProductDto(responseCreatedDto.getId()));

        // and create a dto that is expected as return value
        TranslationDto excpectedTranslationDto = this.modelMapper.map(translation, TranslationDto.class);
        excpectedTranslationDto.setId(expectedNextTranslationId);

        // create the translation.
        String responseTranslation = mockMvc.perform(post(API_PATH_TRANSLATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(translationDto))
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TranslationDto responseTranslationDto = this.jsonMapper.readValue(responseTranslation, TranslationDto.class);
        Assertions.assertEquals(translationDto.getLongDescription(), responseTranslationDto.getLongDescription());
        Assertions.assertEquals(responseCreatedDto.getId(), responseTranslationDto.getProduct().getId());
    }

    private Product createTestProduct() {
        Random random = new Random();
        return new Product(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString().replace("-", "").substring(10),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble(),
                1 + (100 - 1) * random.nextDouble()
        );
    }

    private Translation createTestTranslation() {
        String description = UUID.randomUUID().toString() + UUID.randomUUID() + UUID.randomUUID();
        return new Translation(
                description.substring(0, 20),
                description
        );
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString());
        return category;
    }
}
