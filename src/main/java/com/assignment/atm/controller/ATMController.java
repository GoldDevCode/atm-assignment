package com.assignment.atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.atm.dto.BalanceCheckRequest;
import com.assignment.atm.dto.BalanceCheckResponse;
import com.assignment.atm.dto.CashWithdrawalRequest;
import com.assignment.atm.dto.CashWithdrawalResponse;
import com.assignment.atm.service.ATMService;


@RestController
@RequestMapping("/api")
public class ATMController {
	
  @Autowired
  private ATMService atmService;

    
  //checkBalance API
  @RequestMapping(value = "/atm/checkBalance", method = RequestMethod.POST, headers = {
	"Accept=application/json" }, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BalanceCheckResponse> checkBalance(@RequestBody BalanceCheckRequest balanceCheckRequest) throws Exception {
    
	  BalanceCheckResponse balanceCheckDTO = atmService.checkBalanceAmount(balanceCheckRequest);
	  
	  return new ResponseEntity<>(balanceCheckDTO, HttpStatus.OK);
  }
  
  //Withdraw Money API
  @RequestMapping(value = "/atm/withDrawMoney", method = RequestMethod.POST, headers = {
	"Accept=application/json" }, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CashWithdrawalResponse> withDrawMoney(@RequestBody CashWithdrawalRequest cashWithdrawRequest) throws Exception  {
    
	  CashWithdrawalResponse withdrawalResponse = atmService.withdrawAmount(cashWithdrawRequest);
	 
	  return new ResponseEntity<>(withdrawalResponse, HttpStatus.OK);
  }
  
}
