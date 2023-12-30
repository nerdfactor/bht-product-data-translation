package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import de.bhtberlin.paf2023.productdatatranslation.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for exceptions within the application.
 */

@Slf4j
@RestControllerAdvice
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestController
@RequiredArgsConstructor
public class ExceptionController implements ErrorController {

	private final HttpServletRequest request;

	@ExceptionHandler(EntityNotFoundException.class)
	public HttpEntity<ErrorResponseDto> handleEntityNotFoundException(Exception e) {
		log.error("EntityNotFoundException during request {}.", request.getRequestURI());
		return ErrorResponseDto.createResponseEntity(
				HttpStatus.NOT_FOUND,
				e.getMessage()
		);
	}

	@ExceptionHandler(NullPointerException.class)
	public HttpEntity<ErrorResponseDto> handleNullPointerException(Exception e) {
		log.error("NullPointerException during request {}.", request.getRequestURI());
		return ErrorResponseDto.createResponseEntity(
				HttpStatus.INTERNAL_SERVER_ERROR,
				e.getMessage()
		);
	}

	@ExceptionHandler(Exception.class)
	public HttpEntity<ErrorResponseDto> defaultErrorHandler(Exception e) throws Exception {
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}
		log.error("Exception {} during request {}.", e.getMessage(), request.getRequestURI());
		return ErrorResponseDto.createResponseEntity(
				HttpStatus.INTERNAL_SERVER_ERROR,
				e.getMessage()
		);
	}


	@RequestMapping("/error")
	public HttpEntity<ErrorResponseDto> error(HttpServletRequest request) {
		log.error("Unhandled Exception {} during request {}",
				request.getAttribute("jakarta.servlet.error.status_code"),
				request.getAttribute("jakarta.servlet.error.request_uri"));
		HttpStatus statusCode = Optional
				.ofNullable(HttpStatus.resolve((int) request.getAttribute("jakarta.servlet.error.status_code")))
				.orElse(HttpStatus.INTERNAL_SERVER_ERROR);
		return ErrorResponseDto.createResponseEntity(statusCode, "");
	}
}
