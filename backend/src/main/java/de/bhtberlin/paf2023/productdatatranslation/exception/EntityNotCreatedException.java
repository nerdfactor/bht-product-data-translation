package de.bhtberlin.paf2023.productdatatranslation.exception;

public class EntityNotCreatedException extends RuntimeException{

    public EntityNotCreatedException() {
    }

    public EntityNotCreatedException(String message) {
        super(message);
    }
}
