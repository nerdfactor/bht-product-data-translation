package de.bhtberlin.paf2023.productdatatranslation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bhtberlin.paf2023.productdatatranslation.dto.PictureDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.service.PictureCrudService;
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
 * Test for {@link Picture} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PictureRestControllerTest {

    private static final String API_PATH = "/api/pictures";

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked {@link PictureCrudService} in order to provide
     * mock responses to the tested REST controller.
     */
    @MockBean
    PictureCrudService pictureCrudService;

    /**
     * Check if {@link Picture Pictures} can be listed.
     */
    @Test
    void picturesCanBeListed() throws Exception {
        List<PictureDto> mockDtos = Arrays.asList(
                createTestPicture(1),
                createTestPicture(2)
        );
        List<Picture> mockEntities = mockDtos.stream().map(dto -> this.modelMapper.map(dto, Picture.class)).toList();
        Mockito.when(pictureCrudService.listAllPictures())
                .thenReturn(mockEntities);

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDtos)));
    }

    /**
     * Check if a {@link Picture} can be created.
     */
    @Test
    void pictureCanBeCreated() throws Exception {
        PictureDto mockDto = createTestPicture();
        Mockito.when(pictureCrudService.createPicture(any(Picture.class)))
                .thenReturn(this.modelMapper.map(mockDto, Picture.class));

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(new Picture()))
                ).andExpect(status().isCreated())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Picture} can be read.
     */
    @Test
    void pictureCanBeRead() throws Exception {
        PictureDto mockDto = createTestPicture();
        Mockito.when(pictureCrudService.readPicture(any(int.class)))
                .thenReturn(Optional.of(this.modelMapper.map(mockDto, Picture.class)));

        mockMvc.perform(get(API_PATH + "/" + mockDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Picture} can be updated.
     */
    @Test
    void pictureCanBeUpdated() throws Exception {
        PictureDto mockDto = createTestPicture(1);
        Mockito.when(pictureCrudService.updatePicture(argThat(argument -> argument.getId() == mockDto.getId())))
                .thenReturn(this.modelMapper.map(mockDto, Picture.class));

        mockMvc.perform(put(API_PATH + "/" + mockDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.jsonMapper.writeValueAsString(mockDto))
                ).andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
    }

    /**
     * Check if a {@link Picture} can be deleted.
     */
    @Test
    void pictureCanBeDeleted() throws Exception {
        mockMvc.perform(delete(API_PATH + "/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    private PictureDto createTestPicture() {
        Random random = new Random();
        PictureDto dto = new PictureDto();
        dto.setFilename(UUID.randomUUID().toString().replace("-", "").substring(10));
        dto.setFormat("jpg");
        dto.setWidth(1 + (100 - 1) * random.nextDouble());
        dto.setHeight(1 + (100 - 1) * random.nextDouble());
        return dto;
    }

    private PictureDto createTestPicture(int id) {
        PictureDto dto = createTestPicture();
        dto.setId(id);
        return dto;
    }
}
