package de.bhtberlin.paf2023.productdatatranslation.component;

import de.bhtberlin.paf2023.productdatatranslation.service.translation.AutoTranslatable;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
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
public class AutoTranslateListAdvice implements ResponseBodyAdvice<List<AutoTranslatable>> {

	@Autowired
	Translator translator;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		if (returnType.getGenericParameterType() instanceof ParameterizedType) {
			try {
				Type[] args = ((ParameterizedType) returnType.getGenericParameterType()).getActualTypeArguments();
				if (args[0] instanceof ParameterizedType) {
					Class<?> cls = (Class<?>) ((ParameterizedType) args[0]).getRawType();
					Type[] types = ((ParameterizedType) args[0]).getActualTypeArguments();
					if (cls != null && List.class.isAssignableFrom(cls) && types.length > 0 && AutoTranslatable.class.isAssignableFrom((Class<?>) types[0])) {
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
	public List<AutoTranslatable> beforeBodyWrite(List<AutoTranslatable> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body != null) {
			body.forEach(translatable -> {
				translatable.autoTranslate(translator, LocaleContextHolder.getLocale().toLanguageTag().replace("-", "_"));
			});
		}
		return body;
	}
}
