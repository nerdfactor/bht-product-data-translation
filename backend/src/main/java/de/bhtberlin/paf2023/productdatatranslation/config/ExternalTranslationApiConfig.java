package de.bhtberlin.paf2023.productdatatranslation.config;

import com.deepl.api.Translator;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.ExternalTranslationApi;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.GoogleWebTranslationApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExternalTranslationApiConfig {

	final AppConfig appConfig;

	@Bean
	public ExternalTranslationApi getExternalTranslationApi() {
		// todo: get api from configuration.
		// todo: somehow change api during runtime?
		return new GoogleWebTranslationApi(new JsonMapper());
	}

	@Bean
	public Translate getGoogleCloudTranslate() {
		return TranslateOptions.newBuilder()
				.setProjectId("bht-product-data-translation ")
				.setApiKey(this.appConfig.getApiConfig().getGoogleCloudApiKey()).build().getService();
	}

	@Bean
	public com.deepl.api.Translator getDeeplTranslator() {
		return new Translator(this.appConfig.getApiConfig().getDeeplApiKey());
	}
}
