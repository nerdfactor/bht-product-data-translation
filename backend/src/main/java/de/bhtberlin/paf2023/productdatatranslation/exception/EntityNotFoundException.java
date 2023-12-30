package de.bhtberlin.paf2023.productdatatranslation.exception;

public class EntityNotFoundException extends RuntimeException{

	public EntityNotFoundException() {
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
}
