package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.bhtberlin.paf2023.productdatatranslation.entity.Language;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.TranslationVisitor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The CategoryDto does not implement {@link Translatable#translate(TranslationVisitor, Language, Language)}
 * and instead uses the default implementation. This allows the for more
 * centralization of the logic into the {@link TranslationVisitor} and reduces
 * the boilerplate that has to be implemented in all {@link Translatable Translatables}.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class CategoryDto implements Translatable {

    private int id;

    private String name;

    public CategoryDto(int id) {
        this.id = id;
    }
}
