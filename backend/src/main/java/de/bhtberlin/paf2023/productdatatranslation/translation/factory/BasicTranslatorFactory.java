package de.bhtberlin.paf2023.productdatatranslation.translation.factory;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

public class BasicTranslatorFactory implements TranslatorFactory {

    @Override
    public Translator getTranslator(AppConfig.@NotNull TranslatorConfig config, ListableBeanFactory beanFactory) {
        String translatorClassName = createClassName(config.getTranslator(), config.getTranslatorPackage());
        try {
            return (Translator) this.createClass(Class.forName(translatorClassName), beanFactory);
        } catch (Exception e) {
            throw new RuntimeException("Could not create translator.", e);
        }
    }

    protected @NotNull String createClassName(@NotNull String className, String packageName) {
        if (className.contains(".")) {
            return className;
        }
        return packageName + "." + className;
    }

    protected <T> T createClass(@NotNull Class<T> cls, ListableBeanFactory beanFactory) {
        try {
            return BeanFactoryUtils.beanOfType(beanFactory, cls);
        } catch (Exception e) {
            try {
                return cls.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Could not create class.", e);
            }
        }
    }
}
