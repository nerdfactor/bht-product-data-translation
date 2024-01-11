package de.bhtberlin.paf2023.productdatatranslation.config;

import de.bhtberlin.paf2023.productdatatranslation.picture.Storage;
import de.bhtberlin.paf2023.productdatatranslation.picture.StorageFactory;
import de.bhtberlin.paf2023.productdatatranslation.repo.PictureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PictureConfig {

    final AppConfig appConfig;

    final PictureRepository pictureRepository;

    @Bean
    @Primary
    public Storage getStorage() {
        try {
            return StorageFactory.createStorage(appConfig, pictureRepository);
        } catch (Exception e) {
            throw new RuntimeException("Could not create Storage.", e);
        }
    }
}
