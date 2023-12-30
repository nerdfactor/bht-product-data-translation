package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.service.translation.SimpleStringTranslator;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/translations")
public class TranslationController {

	@PostMapping("/i18n")
	public ResponseEntity<HashMap<String, String>> i18n(@RequestBody final HashMap<String, String> values) {
		Translator translator = new SimpleStringTranslator();
		final String locale = LocaleContextHolder.getLocale().toLanguageTag().replace("-", "_");
		values.forEach((s, s2) -> {
			values.put(s, translator.translate(s2, locale));
		});
		return ResponseEntity.ok(values);
	}
}
