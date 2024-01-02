package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base Implementation of a {@link Translator} and {@link TranslationVisitor}
 * that implements visitors for all {@link Translatable Translatables} but
 * does no actual translation.
 * <br />
 * Extend this Translator in order to reuse the visiting but do concrete
 * translations.
 */
public class BaseTranslator implements Translator, TranslationVisitor {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String translateText(@Nullable String text, String from, String to) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertCurrency(double currency, String from, String to) {
        return currency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double convertMeasurement(double measurement, String from, String to) {
        return measurement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Translatable deferredVisit(Translatable translatable, String from, String to) {
        if (translatable instanceof CategoryDto categoryDto) {
            return this.visit(categoryDto, from, to);
        } else if (translatable instanceof ColorDto colorDto) {
            return this.visit(colorDto, from, to);
        } else if (translatable instanceof ProductDto productDto) {
            return this.visit(productDto, from, to);
        }
        return translatable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductDto visit(ProductDto dto, String from, String to) {
        dto.setName(this.translateText(dto.getName(), from, to));
        dto.getTranslatables().forEach(translatable -> {
            this.deferredVisit(translatable, from, to);
        });
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorDto visit(ColorDto dto, String from, String to) {
        dto.setName(translateText(dto.getName(), from, to));
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryDto visit(CategoryDto dto, String from, String to) {
        dto.setName(translateText(dto.getName(), from, to));
        return dto;
    }
}
