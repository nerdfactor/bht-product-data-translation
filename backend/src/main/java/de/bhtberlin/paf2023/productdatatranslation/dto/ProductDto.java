package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.bhtberlin.paf2023.productdatatranslation.translation.CompositeTranslatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
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
    private Set<CategoryDto> categories;

    @JsonIgnoreProperties({"products"})
    private Set<ColorDto> colors;

    @JsonIgnoreProperties({"product"})
    private Set<PictureDto> pictures;

    @JsonIgnoreProperties({"product", "revisions"})
    private Set<TranslationDto> translations;

    public ProductDto(int id) {
        this.id = id;
    }

    @Override
    public void addTranslatable(@NotNull Translatable translatable) {
        if (translatable instanceof CategoryDto categoryDto && this.categories != null) {
            this.categories.add(categoryDto);
        } else if (translatable instanceof ColorDto colorDto && this.colors != null) {
            this.colors.add(colorDto);
        }
    }

    @Override
    public void removeTranslatable(@NotNull Translatable translatable) {
        if (translatable instanceof CategoryDto categoryDto && this.categories != null) {
            this.categories.remove(categoryDto);
        } else if (translatable instanceof ColorDto colorDto && this.colors != null) {
            this.colors.remove(colorDto);
        }
    }

    @JsonIgnore
    @Override
    public List<Translatable> getTranslatables() {
        return Stream.concat(
                        Optional.ofNullable(this.categories).orElse(new HashSet<>()).stream(),
                        Optional.ofNullable(this.colors).orElse(new HashSet<>()).stream()).
                collect(Collectors.toList());
    }
}
