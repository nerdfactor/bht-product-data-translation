package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.AutoTranslatable;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.Translator;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryDto implements AutoTranslatable {

    private int id;

    private String name;

    @Override
    public void autoTranslate(Translator translator, String locale) {
        this.name = translator.translate(this.name, locale);
    }
}
