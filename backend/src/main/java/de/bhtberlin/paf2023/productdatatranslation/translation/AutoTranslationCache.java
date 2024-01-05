package de.bhtberlin.paf2023.productdatatranslation.translation;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Very simple implementation of a cache for all auto translated values.
 * This will reduce the amount of calls for external translation for often
 * translated values for the ui or static names of objects.
 * <br>
 * todo: this could be refactored to hit the database or some external cache (redis?) or into an interface and multiple implementations.
 */
public class AutoTranslationCache {

    private final Map<String, String> cache = new HashMap<>();

    /**
     * Get the size of the cache.
     *
     * @return The amount of items in the cache.
     */
    public int getCacheSize() {
        return this.cache.size();
    }

    /**
     * Clear the cache.
     */
    public void clearCache() {
        this.cache.clear();
    }

    /**
     * Check if the translation for a text is cached.
     *
     * @param text The text.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return True if the text is cached.
     */
    public boolean textIsCached(@NotNull String text, @NotNull String from, @NotNull String to) {
        return cache.containsKey(this.createCacheId(text, from, to));
    }

    /**
     * Add a text with its translation to the cache.
     *
     * @param text  The text.
     * @param from  The tag of the current locale.
     * @param to    The tag of the target locale.
     * @param value The translated value.
     */
    public void addToCache(@NotNull String text, @NotNull String from, @NotNull String to, @NotNull String value) {
        this.cache.put(this.createCacheId(text, from, to), text);
    }

    /**
     * Get a text from the cache.
     *
     * @param text The text.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return An {@link Optional} containing the translated value or null if it is not cached.
     */
    public Optional<String> getFromCache(@NotNull String text, @NotNull String from, @NotNull String to) {
        return Optional.ofNullable(this.cache.get(this.createCacheId(text, from, to)));
    }

    /**
     * Wrapp translateText call to a {@link Translator} in a check for a cached
     * version of the text.
     *
     * @param text       The text.
     * @param from       The tag of the current locale.
     * @param to         The tag of the target locale.
     * @param translator The {@link Translator} used for translation of the cache was not hit.
     * @return The translated text.
     */
    public String cachedTranslate(@NotNull String text, @NotNull String from, @NotNull String to, @NotNull Translator translator) {
        Optional<String> cached = this.getFromCache(text, from, to);
        String translated = cached.orElse(translator.translateText(text, from, to, false));
        this.addToCache(text, from, to, translated);
        return translated;
    }

    /**
     * Create an id for a text with its pair of current and target locale.
     *
     * @param text The text.
     * @param from The tag of the current locale.
     * @param to   The tag of the target locale.
     * @return The id for the text.
     */
    private String createCacheId(@NotNull String text, @NotNull String from, @NotNull String to) {
        return sha256(String.format("%s:%s:%s", from, to, text));
    }

    /**
     * Get the sha256 hash for a String.
     *
     * @param str The String that should be hashed.
     * @return The hashed String.
     */
    private @NotNull String sha256(@NotNull String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return String.format("%064x", new BigInteger(1, digest));
        } catch (Exception e) {
            return str;
        }
    }
}
