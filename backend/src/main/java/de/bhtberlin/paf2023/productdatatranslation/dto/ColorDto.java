package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
