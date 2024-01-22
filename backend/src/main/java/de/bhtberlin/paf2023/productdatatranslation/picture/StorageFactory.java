package de.bhtberlin.paf2023.productdatatranslation.picture;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.repo.PictureRepository;

import java.util.Map;
import java.util.function.Supplier;

public class StorageFactory {

    static Map<String, Supplier<Storage>> storages;

    public static Storage createStorage(AppConfig appConfig, PictureRepository pictureRepository) {
        if (storages == null) {
            storages = Map.of("file-storage", () -> new FileSystemStorage(appConfig.getPictureConfig().getPicturePath(), pictureRepository),
                    "memory", () -> new MemoryStorage(pictureRepository));
        }

        return storages.get(appConfig.getPictureConfig().getStoreIn()).get();
    }
}
