package de.bhtberlin.paf2023.productdatatranslation.picture;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.repo.PictureRepository;
import de.bhtberlin.paf2023.productdatatranslation.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class FileSystemStorage implements Storage {

    final String picturePath;

    final PictureRepository pictureRepository;

    @Override
    public @NotNull Picture storeImageForPicture(@NotNull Picture picture, @NotNull MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        if (fileName == null || fileName.isEmpty() || fileType == null || fileType.isEmpty()) {
            return picture;
        }
        String storageName = this.createStorageFileName(fileName, fileType);
        Path path = Paths.get(this.picturePath + "/" + storageName);
        try {
            Files.write(path, file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        picture.setFilename(fileName);
        picture.setFormat(fileType);
        this.pictureRepository.save(picture);
        return picture;
    }

    @Override
    public byte[] loadImageForPicture(@NotNull Picture picture) throws EntityNotFoundException {
        try {
            String storageName = this.createStorageFileName(picture.getFilename(), picture.getFormat());
            return Files.readAllBytes(Path.of(this.picturePath + "/" + storageName));
        } catch (Exception e) {
            throw new EntityNotFoundException("Picture with Id " + picture.getId() + " was not found.");
        }
    }

    @Override
    public boolean deleteImageForPicture(@NotNull Picture picture) {
        String storageName = this.createStorageFileName(picture.getFilename(), picture.getFormat());
        try {
            Files.delete(Path.of(this.picturePath + "/" + storageName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Create a normalized storage name for a file.
     *
     * @param orgFileName The original file name.
     * @param mediaType   The media type of the file.
     * @return A normalized filename with file extension.
     */
    private @NotNull String createStorageFileName(@NotNull String orgFileName, @NotNull String mediaType) {
        return HashUtil.sha256(orgFileName) + "." + this.getFileExtension(mediaType);
    }

    /**
     * Get the file extension for a media type.
     *
     * @param mediaType The media type to get the file extension for.
     * @return The file extension or jpeg as fallback.
     */
    private @NotNull String getFileExtension(@NotNull String mediaType) {
        try {
            return mediaType.split("/")[1];
        } catch (Exception e) {
            return "jpeg";
        }

    }
}
