package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.PictureDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
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

/**
 * Controller for {@link Picture} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pictures")
public class PictureController {

    final PictureService pictureService;

    final PictureCrudService pictureCrudService;

    final ModelMapper mapper;


    /**
     * Download the image of a picture.
     *
     * @param id The id of the picture.
     * @return A ResponseEntity with the image as byte[].
     */
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> downloadPicture(@PathVariable final int id) {
        Picture picture = this.pictureCrudService.readPicture(id).orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        byte[] raw = this.pictureService.loadImageForPicture(picture);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(picture.getFormat()));
        return new ResponseEntity<>(raw, headers, HttpStatus.OK);
    }

    /**
     * Upload an image for a picture.
     *
     * @param id   The id of the picture.
     * @param file The file of the image.
     * @return A ResponseEntity with the updated picture.
     */
    @PostMapping(value = "/{id}/file", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PictureDto> uploadPicture(@PathVariable final int id, @RequestPart("file") MultipartFile file) {
        Picture picture = this.pictureCrudService.readPicture(id).orElseThrow(() -> new EntityNotFoundException("Picture with Id " + id + " was not found."));
        picture = this.pictureService.storeImageForPicture(picture, file);
        return ResponseEntity.ofNullable(this.mapper.map(picture, PictureDto.class));
    }
}
