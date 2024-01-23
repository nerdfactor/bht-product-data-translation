package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.PictureDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.service.PictureService;
import de.bhtberlin.paf2023.productdatatranslation.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * Controller for {@link Picture} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pictures")
public class PictureRestController {

    /**
     * The {@link PictureService} for {@link Picture} related features.
     */
    final PictureService pictureService;

    /**
     * The {@link ProductService} for access to {@link Product Products}.
     */
    final ProductService productService;

    /**
     * The {@link ModelMapper} used for mapping between Entity and DTOs.
     */
    final ModelMapper mapper;

    /**
     * List all {@link Picture Pictures}.
     *
     * @return A {@link ResponseEntity} containing a list of {@link PictureDto} objects.
     */
    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<PictureDto>> listAllPictures() {
        return ResponseEntity.ok(this.pictureService.listAllPictures()
                .stream().map(picture -> this.mapper.map(picture, PictureDto.class))
                .toList());
    }

    /**
     * Read a single {@link Picture} by its ID.
     *
     * @param id The ID of the {@link Picture} to read.
     * @return A {@link ResponseEntity} containing the image data as byte array.
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> readPicture(@PathVariable final int id) {
        Picture picture = this.pictureService.readPicture(id)
                .orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        byte[] raw = this.pictureService.loadImageForPicture(picture);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(picture.getFormat()));
        return new ResponseEntity<>(raw, headers, HttpStatus.OK);
    }

    /**
     * Create a new {@link Picture}.
     *
     * @param file           The image file to create.
     * @param productIdParam The ID of the {@link Product} to create the {@link Picture} for.
     * @return A {@link ResponseEntity} containing the created {@link PictureDto} object.
     */
    @PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PictureDto> createPicture(@RequestPart("file") MultipartFile file, @RequestParam("productId") Optional<Integer> productIdParam) {
        int productId = productIdParam.orElseThrow(() -> new RuntimeException("Product ID is missing"));
        Product product = this.productService.readProduct(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with Id " + productId + " was not found."));
        Picture picture = this.pictureService.createNewPicture(product);
        picture = this.pictureService.storeImageForPicture(picture, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(picture, PictureDto.class));
    }

    /**
     * Update an existing {@link Picture}.
     *
     * @param id   The ID of the {@link Picture} to update.
     * @param file The image file to update.
     * @return A {@link ResponseEntity} containing the updated {@link PictureDto} object.
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<PictureDto> setPicture(@PathVariable final int id, @RequestPart("file") MultipartFile file) {
        Picture picture = this.pictureService.readPicture(id)
                .orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        this.pictureService.deleteImageForPicture(picture);
        picture = this.pictureService.storeImageForPicture(picture, file);
        Picture updated = this.pictureService.updatePicture(picture);
        return ResponseEntity.ok(this.mapper.map(updated, PictureDto.class));
    }

    /**
     * Delete an existing {@link Picture}.
     *
     * @param id The ID of the {@link Picture} to delete.
     * @return A {@link ResponseEntity} with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePicture(@PathVariable final int id) {
        Picture picture = this.pictureService.readPicture(id)
                .orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        this.pictureService.deleteImageForPicture(picture);
        this.pictureService.deletePictureById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
