package com.squaddigital.securityApp.locpoll;

public class InvalidParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidParameterException(String parameters) {
		super(parameters);
	}
	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}
	
  }