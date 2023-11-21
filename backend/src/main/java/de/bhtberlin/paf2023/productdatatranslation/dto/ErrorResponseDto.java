package de.bhtberlin.paf2023.productdatatranslation.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponseDto {

    private boolean success = false;

    private boolean repeat = false;

    private int code = 0;

    private String message = "";

    private long timestamp = System.currentTimeMillis() / 1000L;

    public ErrorResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public static ResponseEntity<ErrorResponseDto> createResponseEntity(@NotNull HttpStatus statusCode, @NotNull String message) {
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponseDto(statusCode.value(), message));
    }
}
