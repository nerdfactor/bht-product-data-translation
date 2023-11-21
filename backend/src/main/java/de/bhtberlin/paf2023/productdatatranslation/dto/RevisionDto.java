package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RevisionDto {

    private int id;

    private int version;

    private long timestamp;

    private String shortDescription;

    private String longDescription;

    private boolean correction;

    @JsonIgnoreProperties({"revisions"})
    private TranslationDto translation;
}
