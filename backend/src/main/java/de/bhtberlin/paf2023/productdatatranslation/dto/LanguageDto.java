package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LanguageDto {

    private int id;

    private String name;

    private String currency;

    private String measure;

    private String isoCode;

    public LanguageDto(int id) {
        this.id = id;
    }
}
