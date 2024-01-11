package de.bhtberlin.paf2023.productdatatranslation.translation.factory;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.translation.BasicTranslator;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;

/**
 * Factory for creating {@link BasicTranslator BasicTranslators} and all
 * inheriting classes. Change the Creation of the {@link Translator} by
 * adding a {@link Bean} for it.
 */
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

    /**
     * Create the full class name from the given class name and package name.
     *
     * @param className   The class name to use.
     * @param packageName The package name to use.
     * @return The full class name.
     */
    protected @NotNull String createClassName(@NotNull String className, String packageName) {
        if (className.contains(".")) {
            return className;
        }
        return packageName + "." + className;
    }

    /**
     * Create a class from the given class name. Will try to get a {@link Bean} of the
     * class before creating a new instance directly.
     *
     * @param cls         The class to create.
     * @param beanFactory The bean factory used to create a {@link Bean}.
     * @param <T>         The type of the class.
     * @return The created class.
     */
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
