package de.bhtberlin.paf2023.productdatatranslation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.AutoTranslatable;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.Translator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ColorDto implements AutoTranslatable {

	private int id;

	private String name;

	@Override
	public void autoTranslate(Translator translator, String locale) {
		this.name = translator.translate(this.name, AppConfig.DEFAULT_LANGUAGE, locale);
	}
}
