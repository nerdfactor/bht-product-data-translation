package de.bhtberlin.paf2023.productdatatranslation.component;

import de.bhtberlin.paf2023.productdatatranslation.config.AppConfig;
import de.bhtberlin.paf2023.productdatatranslation.service.TranslationService;
import de.bhtberlin.paf2023.productdatatranslation.translation.Translatable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class AutoTranslatePageAdvice implements ResponseBodyAdvice<Page<Translatable>> {

    final TranslationService translationService;

    @Override
    public boolean supports(MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getGenericParameterType() instanceof ParameterizedType) {
            try {
                Type[] args = ((ParameterizedType) returnType.getGenericParameterType()).getActualTypeArguments();
                if (args[0] instanceof ParameterizedType) {
                    Class<?> cls = (Class<?>) ((ParameterizedType) args[0]).getRawType();
                    Type[] types = ((ParameterizedType) args[0]).getActualTypeArguments();
                    if (cls != null && Page.class.isAssignableFrom(cls) && types.length > 0 && Translatable.class.isAssignableFrom((Class<?>) types[0])) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public Page<Translatable> beforeBodyWrite(Page<Translatable> body,
                                              @NotNull MethodParameter returnType,
                                              @NotNull MediaType selectedContentType,
                                              @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                              @NotNull ServerHttpRequest request,
                                              @NotNull ServerHttpResponse response) {
        if (body != null) {
            body.getContent().forEach(translatable -> {
                this.translationService.translate(translatable, AppConfig.DEFAULT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());
            });
        }
        return body;
    }
}
