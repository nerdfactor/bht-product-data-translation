package de.bhtberlin.paf2023.productdatatranslation.translation;

import de.bhtberlin.paf2023.productdatatranslation.dto.CategoryDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ColorDto;
import de.bhtberlin.paf2023.productdatatranslation.dto.ProductDto;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
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
public class BasicTranslator implements Translator {

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
    public Translatable deferredVisit(Translatable translatable, Language from, Language to) {
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
    public ProductDto visit(ProductDto dto, Language from, Language to) {
        dto.setName(this.translateText(dto.getName(), from.getIsoCode(), to.getIsoCode()));
        dto.setPrice(this.convertCurrency(dto.getPrice(), from.getCurrency().getIsoCode(), to.getCurrency().getIsoCode()));
        dto.setDepth(this.convertMeasurement(dto.getDepth(), from.getMeasurement().getDepth(), to.getMeasurement().getDepth()));
        dto.setHeight(this.convertMeasurement(dto.getHeight(), from.getMeasurement().getHeight(), to.getMeasurement().getHeight()));
        dto.setWidth(this.convertMeasurement(dto.getWidth(), from.getMeasurement().getWidth(), to.getMeasurement().getWidth()));
        dto.setWeight(this.convertMeasurement(dto.getWeight(), from.getMeasurement().getWeight(), to.getMeasurement().getWeight()));
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorDto visit(ColorDto dto, Language from, Language to) {
        dto.setName(this.translateText(dto.getName(), from.getIsoCode(), to.getIsoCode()));
        // assume there are more steps for translating a ColorDto and not
        // just the same as the other Translatable implementations.
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryDto visit(CategoryDto dto, Language from, Language to) {
        dto.setName(this.translateText(dto.getName(), from.getIsoCode(), to.getIsoCode()));
        // assume there are more steps for translating a CategoryDto and not
        // just the same as the other Translatable implementations.
        return dto;
    }
}
