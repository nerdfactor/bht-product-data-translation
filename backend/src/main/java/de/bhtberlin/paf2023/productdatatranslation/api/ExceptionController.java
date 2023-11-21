package de.bhtberlin.paf2023.productdatatranslation.api;

import de.bhtberlin.paf2023.productdatatranslation.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

/**
 * Controller for exceptions within the application.
 */
@Slf4j
@AllArgsConstructor
@ControllerAdvice
@Controller
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionController extends ResponseEntityExceptionHandler implements ErrorController {

    @ExceptionHandler(EntityNotFoundException.class)
    public HttpEntity<ErrorResponseDto> handleEntityNotFoundException(HttpRequest request, Exception e) {
        log.error("EntityNotFoundException during request {}.", request.getURI());
        return ErrorResponseDto.createResponseEntity(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public HttpEntity<ErrorResponseDto> handleNullPointerException(HttpRequest request, Exception e) {
        log.error("NullPointerException during request {}.", request.getURI());
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
        log.error("Handler: Unhandled Exception", e);
        return ErrorResponseDto.createResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }

    @RequestMapping("/error")
    public HttpEntity<ErrorResponseDto> error(HttpServletRequest request) {
        log.error("Controller: Unhandled Exception {} {}",
                request.getAttribute("jakarta.servlet.error.status_code"),
                request.getAttribute("jakarta.servlet.error.request_uri"));
        HttpStatus statusCode = Optional
                .ofNullable(HttpStatus.resolve((int) request.getAttribute("jakarta.servlet.error.status_code")))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        return ErrorResponseDto.createResponseEntity(statusCode, "");
    }
}
