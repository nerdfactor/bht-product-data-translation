package de.bhtberlin.paf2023.productdatatranslation.translation.caching;

import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A proxy for a {@link Translator} that caches the results of the
 * text translation.
 */
@RequiredArgsConstructor
public class CachedTranslatorProxy implements Translator {

    /**
     * The {@link Translator} to proxy.
     */
    private final Translator translator;

    /**
     * The {@link AutoTranslationCache} to use.
     */
    private final AutoTranslationCache cache;

    /**
     * {@inheritDoc}
     */
    @Override
    public Translatable deferredVisit(Translatable translatable, Language from, Language to) {
        return this.translator.deferredVisit(translatable, from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductDto visit(ProductDto dto, Language from, Language to) {
        return this.translator.visit(dto, from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorDto visit(ColorDto dto, Language from, Language to) {
        return this.translator.visit(dto, from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryDto visit(CategoryDto dto, Language from, Language to) {
        return this.translator.visit(dto, from, to);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Instead of just passing the request to the {@link Translator}, this
     * method will check if the text is already cached and return the cached
     * version.
     */
    @Override
    public @NotNull String translateText(@Nullable String text, String from, String to) {
        if(text == null) {
            return "";
        }
        Optional<String> cached = this.cache.getFromCache(text, from, to);
        String translated = cached.orElse(this.translator.translateText(text, from, to));
        this.cache.addToCache(text, from, to, translated);
        return translated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertCurrency(double currency, String from, String to) {
        return this.translator.convertCurrency(currency, from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertMeasurement(double measurement, String from, String to) {
        return this.translator.convertMeasurement(measurement, from, to);
    }
}
