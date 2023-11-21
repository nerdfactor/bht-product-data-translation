package de.bhtberlin.paf2023.productdatatranslation.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic Api configuration.
 */
@Configuration
public class ApiConfig {

    /**
     * Provides a bean of {@link ModelMapper} to map between models.
     *
     * @return A new ModelMapper
     */
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
}
