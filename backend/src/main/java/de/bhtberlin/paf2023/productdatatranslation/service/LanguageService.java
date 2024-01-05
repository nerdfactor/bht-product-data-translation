package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class LanguageService {

    /**
     * An implementation of a {@link LanguageRepository} for data access.
     * For example a specific JpaRepository for access to database layer.
     */
    final LanguageRepository languageRepository;

    /**
     * Get the default {@link Language} of the system.
     *
     * @return The default {@link Language}.
     */
    public Language getDefaultLanguage() {
        return this.languageRepository.findOneByIsoCode(AppConfig.DEFAULT_LANGUAGE).orElseThrow();
    }

    /**
     * Normalize a language tag to conform to ISO639, ISO3166 and IETF so
     * that Java, the Http Requests and external Apis can use the same tag
     * format.
     *
     * @param tag The original language tag.
     * @return The normalized language tag.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc5646">rfc5646</a>
     * @see <a href="https://en.wikipedia.org/wiki/ISO_639">ISO_639</a>
     * @see <a href="https://en.wikipedia.org/wiki/ISO_3166">ISO_3166</a>
     * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF</a>
     * @see <a href="https://www.w3.org/TR/html401/struct/dirlang.html">W3C Lang</a>
     */
    public static @NotNull String normalizeLanguageTag(@NotNull String tag) {
        tag = tag.replace("_", "-");
        if (tag.length() > 2) {
            String[] parts = tag.split("-");
            if (parts[0].equalsIgnoreCase(parts[1])) {
                tag = parts[0];
            }
        }
        if (tag.equalsIgnoreCase("en-gb") || tag.equalsIgnoreCase("en-us")) {
            // dirty fix for different tags für en-GB and en-US until we also
            // support multiple languages for them.
            tag = "en";
        }
        if (tag.isEmpty()) {
            return AppConfig.DEFAULT_LANGUAGE;
        }
        return Locale.forLanguageTag(tag).toLanguageTag();
    }
}
