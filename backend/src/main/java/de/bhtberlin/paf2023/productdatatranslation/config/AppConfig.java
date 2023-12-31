package de.bhtberlin.paf2023.productdatatranslation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application specific configuration.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

	/**
	 * The current application version.
	 */
	private String version;

	/**
	 * The configuration for external Apis.
	 */
	private ExternalApiConfig apiConfig;

	@Getter
	@Setter
	public static class ExternalApiConfig {

		private String deeplApiKey;
		private String googleCloudApiKey;
	}
}
