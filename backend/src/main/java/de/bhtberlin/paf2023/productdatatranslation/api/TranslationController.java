package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller for translation related operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    /**
     * The {@link TranslatorService} for translation related features.
     */
    final TranslatorService translatorService;

    /**
     * Translate a map of strings for internationalization.
     *
     * @param values The map of strings to translate.
     * @return A {@link ResponseEntity} containing the translated strings.
     */
    @PostMapping("/i18n")
    public ResponseEntity<Map<String, String>> i18n(@RequestBody final Map<String, String> values) {
        final String locale = LocaleContextHolder.getLocale().toLanguageTag().replace("-", "_");
        values.forEach((s, s2) -> values.put(s, this.translatorService.translateString(s2, AppConfig.DEFAULT_LANGUAGE, locale)));
        return ResponseEntity.ok(values);
    }
}
