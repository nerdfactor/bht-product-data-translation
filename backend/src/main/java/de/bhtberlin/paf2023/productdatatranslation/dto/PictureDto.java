package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PictureDto {

    private int id;

    private String filename;

    private String format;

    private double height;

    private double width;

    @JsonIgnoreProperties({"pictures"})
    private ProductDto product;
}
