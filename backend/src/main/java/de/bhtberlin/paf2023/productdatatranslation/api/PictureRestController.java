package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.PictureDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.service.PictureCrudService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for {@link Picture} related REST operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pictures")
public class PictureRestController {

    final PictureCrudService pictureCrudService;

    final ModelMapper mapper;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<PictureDto>> listAllPictures() {
        return ResponseEntity.ok(this.pictureCrudService.listAllPictures()
                .stream().map(picture -> this.mapper.map(picture, PictureDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readPicture(@PathVariable final int id) {
        Optional<Picture> picture = this.pictureCrudService.readPicture(id);
        if (picture.isEmpty()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.NOT_FOUND,
                    "Picture with Id " + id + " was not found."
            );
        }
        return ResponseEntity.ok(this.mapper.map(picture.get(), PictureDto.class));
    }

    /**
     * Create a new {@link Picture} .
     * <p>
     * This method will enforce plain {@link Picture} creation by removing all linked entities.
     */
    @PostMapping("")
    public ResponseEntity<PictureDto> createPicture(@RequestBody final PictureDto dto) {
        dto.setProduct(null);
        Picture created = this.pictureCrudService.createPicture(this.mapper.map(dto, Picture.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mapper.map(created, PictureDto.class));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> setPicture(@PathVariable final int id, @RequestBody final PictureDto dto) {
        if (id != dto.getId()) {
            return ErrorResponseDto.createResponseEntity(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Mismatch between provided Ids."
            );
        }
        Picture updated = this.pictureCrudService.updatePicture(this.mapper.map(dto, Picture.class));
        return ResponseEntity.ok(this.mapper.map(updated, PictureDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePicture(@PathVariable final int id) {
        this.pictureCrudService.deletePictureById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
