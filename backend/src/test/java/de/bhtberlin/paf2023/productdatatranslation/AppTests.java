package de.bhtberlin.paf2023.productdatatranslation;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Basic Application tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class AppTests {


    /**
     * Check if the Spring ApplicationContext is loaded after starting the Application.
     *
     * @param applicationContext The application context.
     */
    @Test
    void springApplicationContextLoads(@Autowired ApplicationContext applicationContext) {
        assertNotNull(applicationContext);
    }

    /**
     * Check if the application configuration loads correctly during application startup
     * by checking for some of the loaded properties.
     *
     * @param appConfig   The application configuration.
     * @param environment The application environment.
     */
    @Test
    void applicationConfigurationLoads(@Autowired AppConfig appConfig, @Autowired Environment environment) {
        assertNotNull(appConfig);
        String version = appConfig.getVersion();
        assertNotNull(version);
        assertFalse(version.isEmpty());
        String name = environment.getProperty("spring.application.name");
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }
}
