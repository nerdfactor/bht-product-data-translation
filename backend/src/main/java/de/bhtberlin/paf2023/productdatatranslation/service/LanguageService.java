package de.bhtberlin.paf2023.productdatatranslation.service;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.exception.TranslationException;
import de.bhtberlin.paf2023.productdatatranslation.repo.LanguageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Service for operations on Languages.
 */
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
     * Will return a list of all {@link Language Languages}.
     * This list may be empty, if no Languages are present.
     *
     * @return A List of {@link Language Languages}
     */
    public @NonNull List<Language> listAllLanguages() {
        return this.languageRepository.findAll();
    }

    /**
     * Create a Language.
     *
     * @param language The Language, that should be created.
     * @return The Language, that was created.
     */
    public @NotNull Language createLanguage(@NotNull Language language) {
        return this.languageRepository.save(language);
    }

    /**
     * Read a Language.
     *
     * @param id The id for the Language.
     * @return An optional containing the found Language.
     */
    public @NotNull Optional<Language> readLanguage(int id) {
        return this.languageRepository.findById(id);
    }

    /**
     * Get the default {@link Language} of the system.
     *
     * @return The default {@link Language}.
     */
    public Language getDefaultLanguage() {
        return this.languageRepository.findOneByIsoCode(AppConfig.DEFAULT_LANGUAGE).orElseThrow();
    }

    /**
     * Get a {@link Language} by its ISO code.
     *
     * @param isoCode The ISO code of the {@link Language}.
     * @return The {@link Language} with the given ISO code.
     */
    public Language getByIsoCode(String isoCode) {
        return this.languageRepository.findOneByIsoCode(isoCode)
                .orElseThrow(() -> new TranslationException("Could not find Language " + isoCode + "."));
    }

    /**
     * Update a Language.
     *
     * @param language The Language with updated values.
     * @return The updated Language.
     */
    public @NotNull Language updateLanguage(@NotNull Language language) {
        return this.languageRepository.save(language);
    }

    /**
     * Delete a Language.
     *
     * @param language The Language to delete.
     */
    public void deleteLanguage(@NotNull Language language) {
        this.languageRepository.delete(language);
    }

    /**
     * Delete a Language by its id.
     *
     * @param id The id of the Language to delete.
     */
    public void deleteLanguageById(int id) {
        this.languageRepository.deleteById(id);
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
