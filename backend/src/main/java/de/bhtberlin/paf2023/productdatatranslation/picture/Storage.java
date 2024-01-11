package de.bhtberlin.paf2023.productdatatranslation.picture;

import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface Storage {
    public @NotNull Picture storeImageForPicture(@NotNull Picture picture, @NotNull MultipartFile file);
    public byte[] loadImageForPicture(@NotNull Picture picture) throws EntityNotFoundException;
    public boolean deleteImageForPicture(@NotNull Picture picture);
}
