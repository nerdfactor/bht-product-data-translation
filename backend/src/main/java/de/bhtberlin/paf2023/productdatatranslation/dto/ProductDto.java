package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.bhtberlin.paf2023.productdatatranslation.translation.CompositeTranslatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDto implements CompositeTranslatable {

    private int id;

    private String serial;

    private String name;

    private double height;

    private double width;

    private double depth;

    private double weight;

    private double price;

    @JsonIgnoreProperties({"products"})
    private Set<CategoryDto> categories = new HashSet<>();

    @JsonIgnoreProperties({"products"})
    private Set<ColorDto> colors = new HashSet<>();

    @JsonIgnoreProperties({"product"})
    private Set<PictureDto> pictures = new HashSet<>();

    @JsonIgnoreProperties({"product", "revisions"})
    private Set<TranslationDto> translations = new HashSet<>();

    @Override
    public void addTranslatable(@NotNull Translatable translatable) {
        if (translatable instanceof CategoryDto categoryDto) {
            this.categories.add(categoryDto);
        } else if (translatable instanceof ColorDto colorDto) {
            this.colors.add(colorDto);
        }
    }

    @Override
    public void removeTranslatable(@NotNull Translatable translatable) {
        if (translatable instanceof CategoryDto categoryDto) {
            this.categories.remove(categoryDto);
        } else if (translatable instanceof ColorDto colorDto) {
            this.colors.remove(colorDto);
        }
    }

    @Override
    public List<Translatable> getTranslatables() {
        return Stream.concat(
                        this.categories.stream(),
                        this.colors.stream()).
                collect(Collectors.toList());
    }
}
