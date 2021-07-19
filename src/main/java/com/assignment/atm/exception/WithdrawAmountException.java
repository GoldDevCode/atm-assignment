package com.assignment.atm.exception;

public class WithdrawAmountException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	  public WithdrawAmountException(String msg) {
	    super(msg);
	  }

}
