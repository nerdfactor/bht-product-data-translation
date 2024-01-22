package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.picture.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class PictureService {

    final Storage storage;

    /**
     * Store an image and update the metadata of the picture.
     *
     * @param picture The picture this image is stored for.
     * @param file    The file to store.
     * @return The updated picture.
     */
    public @NotNull Picture storeImageForPicture(@NotNull Picture picture, @NotNull MultipartFile file) {
        return this.storage.storeImageForPicture(picture, file);
    }

    /**
     * Load an image (as byte[]) of a picture.
     *
     * @param picture The picture the image is loaded for.
     * @return A byte[]
     * @throws EntityNotFoundException If the image can't be found.
     */
    public byte[] loadImageForPicture(@NotNull Picture picture) throws EntityNotFoundException {
        return this.storage.loadImageForPicture(picture);
    }

    /**
     * Deletes an image of a picture.
     *
     * @param picture The picture the image to be deleted.
     * @return true if deleted, false otherwise
     */
    public boolean deleteImageForPicture(@NotNull Picture picture) {
        return this.storage.deleteImageForPicture(picture);
    }
}
