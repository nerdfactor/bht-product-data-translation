package de.bhtberlin.paf2023.productdatatranslation.exception;

public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException() {
    }

    public UnprocessableEntityException(String message) {
        super(message);
    }
}
