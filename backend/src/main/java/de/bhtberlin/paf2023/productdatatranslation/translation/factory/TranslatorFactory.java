package de.bhtberlin.paf2023.productdatatranslation.translation.factory;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * Factory for creating {@link Translator Translators}.
 */
public interface TranslatorFactory {

    Translator getTranslator(@NotNull AppConfig.TranslatorConfig config, ListableBeanFactory beanFactory) throws ClassNotFoundException;
}
