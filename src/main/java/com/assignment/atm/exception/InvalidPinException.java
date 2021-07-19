package com.assignment.atm.exception;

public class InvalidPinException extends RuntimeException {
	
	  private static final long serialVersionUID = 1L;

	  public InvalidPinException(String msg) {
	    super(msg);
	  }

}
