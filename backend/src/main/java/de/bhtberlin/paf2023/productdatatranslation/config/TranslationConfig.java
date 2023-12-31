package de.bhtberlin.paf2023.productdatatranslation.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.ExternalTranslationApi;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.FullTextTranslator;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.GoogleWebTranslationApi;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class TranslationConfig {

	final AppConfig appConfig;

	@Bean
	@Primary
	public Translator getTranslator() {
		// todo: get from configuration.
		// todo: somehow change during runtime?
		return new FullTextTranslator(getExternalTranslationApi());
	}

	@Bean
	@Primary
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
		return new com.deepl.api.Translator(this.appConfig.getApiConfig().getDeeplApiKey());
	}
}
