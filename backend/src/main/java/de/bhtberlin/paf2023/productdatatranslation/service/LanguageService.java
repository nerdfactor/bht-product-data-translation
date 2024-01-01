package de.bhtberlin.paf2023.productdatatranslation.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LanguageService {


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
        return Locale.forLanguageTag(tag).toLanguageTag();
    }
}
