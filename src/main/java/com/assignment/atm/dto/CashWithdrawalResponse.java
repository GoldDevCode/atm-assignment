package com.assignment.atm.dto;

import com.assignment.atm.exception.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CashWithdrawalResponse {
	
	public String notesDispensed;
	
	public String remainingBalance;
	
	public String overDraft;
		
	public ErrorResponse errorResponse;
	

}
