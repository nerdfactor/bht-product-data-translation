package de.bhtberlin.paf2023.productdatatranslation.picture;

import de.bhtberlin.paf2023.productdatatranslation.entity.Picture;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import de.bhtberlin.paf2023.productdatatranslation.repo.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RequiredArgsConstructor
public class MemoryStorage implements Storage {

    final private PictureRepository pictureRepository;

    private static HashMap<Integer, byte[]> storage = new HashMap<>();

    @Override
    public @NotNull Picture storeImageForPicture(@NotNull Picture picture, @NotNull MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        if (fileName == null || fileName.isEmpty() || fileType == null || fileType.isEmpty()) {
            return picture;
        }
        try {
            storage.put(picture.getId(), file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        picture.setFilename(fileName);
        picture.setFormat(fileType);
        // todo: maybe remove reference to repository by returning the unsaved picture back to the service
        this.pictureRepository.save(picture);
        return picture;
    }

    @Override
    public byte[] loadImageForPicture(@NotNull Picture picture) throws EntityNotFoundException {
        return storage.get(picture.getId());
    }

    @Override
    public boolean deleteImageForPicture(@NotNull Picture picture) {
        if (!storage.containsKey(picture.getId())) {
            return false;
        }

        storage.remove(picture.getId());
        return true;
    }
}
