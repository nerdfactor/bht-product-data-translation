package de.bhtberlin.paf2023.productdatatranslation.component;

import de.bhtberlin.paf2023.productdatatranslation.service.translation.AutoTranslatable;
import de.bhtberlin.paf2023.productdatatranslation.service.AutoTranslationService;
import de.bhtberlin.paf2023.productdatatranslation.service.translation.SimpleStringTranslator;
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

@ControllerAdvice
public class AutoTranslateAdvice implements ResponseBodyAdvice<AutoTranslatable> {

	@Autowired
	AutoTranslationService AutoTranslationService;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		if (returnType.getGenericParameterType() instanceof ParameterizedType) {
			try {
				Type[] args = ((ParameterizedType) returnType.getGenericParameterType()).getActualTypeArguments();
				return AutoTranslatable.class.isAssignableFrom((Class<?>) args[0]);
			}catch (Exception e){
				return false;
			}
		}
		return false;
	}

	@Override
	public AutoTranslatable beforeBodyWrite(AutoTranslatable body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body != null) {
			body.autoTranslate(new SimpleStringTranslator(), LocaleContextHolder.getLocale().toLanguageTag().replace("-", "_"));
		}
		return body;
	}
}
