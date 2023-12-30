package de.bhtberlin.paf2023.productdatatranslation.service.translation;

import de.bhtberlin.paf2023.productdatatranslation.service.translation.Translator;

public interface AutoTranslatable {

	void autoTranslate(Translator translator, String locale);
}
