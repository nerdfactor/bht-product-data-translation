package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LanguageDto {

    private int id;

    private String name;

    private String currency;

    private String measure;

    private String isoCode;

    @JsonIgnoreProperties({"language"})
    private Set<TranslationDto> translations;
}
