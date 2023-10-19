package com.concerto.crud.config.exception;
public class JsonConversionException extends RuntimeException {

	
	 private static final long serialVersionUID = 1L; 
	 
    public JsonConversionException(String message) {
        super(message);
    }

    public JsonConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
