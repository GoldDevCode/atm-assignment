package com.assignment.atm.dto;

import com.assignment.atm.exception.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BalanceCheckResponse {
	
	public String accountNumber;
	
	public String balanceInAccount;
	
	public String maximumWithdrawableAmount;
	
	public ErrorResponse errorResponse;

}
