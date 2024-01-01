package de.bhtberlin.paf2023.productdatatranslation.exception;

public class TranslationException extends RuntimeException{

    public TranslationException() {
    }

    public TranslationException(String message) {
        super(message);
    }
}
