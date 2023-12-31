package de.bhtberlin.paf2023.productdatatranslation.service.translation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FullTextTranslator implements Translator {

	final ExternalTranslationApi externalTranslationApi;

	@Override
	public String translate(String string, String from, String to) {
		return externalTranslationApi.translate(string, from, to);
	}
}
