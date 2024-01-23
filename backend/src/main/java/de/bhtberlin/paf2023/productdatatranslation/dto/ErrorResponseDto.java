package de.bhtberlin.paf2023.productdatatranslation.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Basic wrapper for error responses containing useful information about
 * the error that can be acted on by the client.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponseDto {

    /**
     * Whether the request was successful or not.
     */
    private boolean success = false;

    /**
     * Whether the request should be repeated or not.
     */
    private boolean repeat = false;

    /**
     * The http status code.
     */
    private int code = 0;

    /**
     * The error message.
     */
    private String message = "";

    /**
     * The timestamp of the error.
     */
    private long timestamp = System.currentTimeMillis() / 1000L;

    public ErrorResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Creates a new {@link ResponseEntity} with the given status code and message.
     *
     * @param statusCode The status code to use.
     * @param message    The message to use.
     * @return The created {@link ResponseEntity}.
     */
    public static ResponseEntity<ErrorResponseDto> createResponseEntity(@NotNull HttpStatus statusCode, @NotNull String message) {
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponseDto(statusCode.value(), message));
    }
}
