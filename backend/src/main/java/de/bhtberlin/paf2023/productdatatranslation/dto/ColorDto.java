package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import de.bhtberlin.paf2023.productdatatranslation.translation.TranslationVisitor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The ColorDto does not implement {@link Translatable#translate(TranslationVisitor, String, String)}
 * and instead uses the default implementation. This allows the for more
 * centralization of the logic into the {@link TranslationVisitor} and reduces
 * the boilerplate that has to be implemented in all {@link Translatable Translatables}.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class ColorDto implements Translatable {

    private int id;

    private String name;

    public ColorDto(int id) {
        this.id = id;
    }
}
