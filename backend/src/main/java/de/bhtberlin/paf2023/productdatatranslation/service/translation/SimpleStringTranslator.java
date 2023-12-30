package de.bhtberlin.paf2023.productdatatranslation.service.translation;

import java.util.regex.Pattern;

public class SimpleStringTranslator implements Translator {

	public String translate(String string, String locale) {
		return this.replaceLocalePrefix(string, locale + ":");
	}

	private String replaceLocalePrefix(String text, String newPrefix) {
		String prefixPattern = "^[a-z]{2}(_[A-Z]{2})?:";
		if (Pattern.matches(prefixPattern, text)) {
			text = text.replaceFirst(prefixPattern, newPrefix);
		} else {
			text = newPrefix + text;
		}
		return text;

	}
}
