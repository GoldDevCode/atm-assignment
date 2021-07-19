package com.assignment.atm.constants;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConstants {
	
	
	public static final int MAX_PIN_RETRY_COUNT = 3;
	
	public static final String ERROR_CODE_ACCOUNT_NUMBER_NOT_PROVIDED = "2000";
	public static final String ERROR_CODE_PIN_NOT_PROVIDED = "2001";
	public static final String ERROR_CODE_INVALID_PIN = "2002";
	public static final String ERROR_CODE_INVALID_AMOUNT = "2003";
	public static final String ERROR_CODE_INSUFFICIENT_BALANCE = "2004";
	public static final String ERROR_CODE_INSUFFICIENT_FUND_IN_ATM = "2005";
	public static final String ERROR_CODE_CARD_LOCKED_OUT = "2006";
	public static final String ERROR_CODE_DENOMINATION_NOT_VALID = "2007";
	
	
	
	public static  Map<String,String> errorCodeMessageMap = new HashMap<String,String>();
	
	static {
		errorCodeMessageMap.put(ERROR_CODE_ACCOUNT_NUMBER_NOT_PROVIDED, "Account Number not provided");
		errorCodeMessageMap.put(ERROR_CODE_PIN_NOT_PROVIDED, "Pin Number not provided");
		errorCodeMessageMap.put(ERROR_CODE_INVALID_PIN, "Account Number Pin combination not valid.Card access will be locked after ");
		errorCodeMessageMap.put(ERROR_CODE_INVALID_AMOUNT, "Entered Amount not valid");
		errorCodeMessageMap.put(ERROR_CODE_INSUFFICIENT_BALANCE, "Cannot withdraw requested amount.Insufficient balance in account");
		errorCodeMessageMap.put(ERROR_CODE_INSUFFICIENT_FUND_IN_ATM, "Insufficient funds in ATM");
		errorCodeMessageMap.put(ERROR_CODE_CARD_LOCKED_OUT, "Card has been locked out.Please get in touch with your bank branch for assistance");
		errorCodeMessageMap.put(ERROR_CODE_DENOMINATION_NOT_VALID, "Please enter amount in multiple of 10 or 5");
	}
	
}
