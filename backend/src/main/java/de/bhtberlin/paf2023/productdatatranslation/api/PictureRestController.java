package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.PictureDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.exception.UnprocessableEntityException;
import de.bhtberlin.paf2023.productdatatranslation.service.PictureCrudService;
import de.bhtberlin.paf2023.productdatatranslation.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.util.List;

/**
 * Controller for {@link Picture} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pictures")
public class PictureRestController {

    final PictureService pictureService;

    final PictureCrudService pictureCrudService;

    final ModelMapper mapper;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<PictureDto>> listAllPictures() {
        return ResponseEntity.ok(this.pictureCrudService.listAllPictures()
                .stream().map(picture -> this.mapper.map(picture, PictureDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> readPicture(@PathVariable final int id) {
        Picture picture = this.pictureCrudService.readPicture(id).
                orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        byte[] raw = this.pictureService.loadImageForPicture(picture);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(picture.getFormat()));
        return new ResponseEntity<>(raw, headers, HttpStatus.OK);
    }

    /**
     * Create a new {@link Picture} .
     * <p>
     * This method will enforce plain {@link Picture} creation by removing all linked entities.
     */
    @PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PictureDto> createPicture(@RequestPart("file") MultipartFile file) {
        Picture picture = this.pictureCrudService.createNewPicture();
        picture = this.pictureService.storeImageForPicture(picture, file);
        return ResponseEntity.ofNullable(this.mapper.map(picture, PictureDto.class));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<PictureDto> setPicture(@PathVariable final int id, @RequestPart("file") MultipartFile file) {
        Picture picture = this.pictureCrudService.readPicture(id)
                .orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        this.pictureService.deleteImageForPicture(picture);
        picture = this.pictureService.storeImageForPicture(picture, file);
        Picture updated = this.pictureCrudService.updatePicture(picture);
        return ResponseEntity.ok(this.mapper.map(updated, PictureDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePicture(@PathVariable final int id) {
        Picture picture = this.pictureCrudService.readPicture(id)
                .orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        this.pictureService.deleteImageForPicture(picture);
        this.pictureCrudService.deletePictureById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
