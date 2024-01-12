package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.entity.Product;
import de.bhtberlin.paf2023.productdatatranslation.repo.PictureRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service for basic CRUD operations on Pictures.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PictureCrudService {

    /**
     * An implementation of a {@link PictureRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final PictureRepository pictureRepository;

    /**
     * Will return a list of all {@link Picture Pictures}.
     * This list may be empty, if no Pictures are present.
     *
     * @return A List of {@link Picture Pictures}
     */
    public @NonNull List<Picture> listAllPictures() {
        return this.pictureRepository.findAll();
    }

    /**
     * Creates a new Picture.
     *
     * @return The Picture, that was created.
     */
    public @NotNull Picture createNewPicture(Product product) {
        Picture picture = new Picture();
        picture.setProduct(product);
        return this.pictureRepository.save(picture);
    }

    /**
     * Create a Picture.
     *
     * @param picture The Picture, that should be created.
     * @return The Picture, that was created.
     */
    public @NotNull Picture createPicture(@NotNull Picture picture) {
        return this.pictureRepository.save(picture);
    }

    /**
     * Read a Picture.
     *
     * @param id The id for the Picture.
     * @return An optional containing the found Picture.
     */
    public @NotNull Optional<Picture> readPicture(int id) {
        return this.pictureRepository.findById(id);
    }

    /**
     * Update a Picture.
     *
     * @param picture The Picture with updated values.
     * @return The updated Picture.
     */
    public @NotNull Picture updatePicture(@NotNull Picture picture) {
        return this.pictureRepository.save(picture);
    }

    /**
     * Delete a Picture.
     *
     * @param picture The Picture to delete.
     */
    public void deletePicture(@NotNull Picture picture) {
        this.pictureRepository.delete(picture);
    }

    /**
     * Delete a Picture by its id.
     *
     * @param id The id of the Picture to delete.
     */
    public void deletePictureById(int id) {
        this.pictureRepository.deleteById(id);
    }
}
