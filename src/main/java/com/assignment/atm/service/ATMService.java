package com.assignment.atm.service;

import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_ACCOUNT_NUMBER_NOT_PROVIDED;
import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_CARD_LOCKED_OUT;
import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_DENOMINATION_NOT_VALID;
import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_INSUFFICIENT_BALANCE;
import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_INSUFFICIENT_FUND_IN_ATM;
import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_INVALID_AMOUNT;
import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_INVALID_PIN;
import static com.assignment.atm.constants.ApplicationConstants.ERROR_CODE_PIN_NOT_PROVIDED;
import static com.assignment.atm.constants.ApplicationConstants.MAX_PIN_RETRY_COUNT;
import static com.assignment.atm.constants.ApplicationConstants.errorCodeMessageMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.atm.dto.BalanceCheckRequest;
import com.assignment.atm.dto.BalanceCheckResponse;
import com.assignment.atm.dto.CashWithdrawalRequest;
import com.assignment.atm.dto.CashWithdrawalResponse;
import com.assignment.atm.exception.ErrorResponse;
import com.assignment.atm.model.ATMMachine;
import com.assignment.atm.model.Account;

@Service
public class ATMService {
	
	private Logger logger = LoggerFactory.getLogger(ATMService.class);
	
	public ATMMachine atmMachineInstance;
	
	public void initializeATM() {
		
		logger.info("Initialization method called");
		
		double intialAmountInATM = 1500;
		
		ATMMachine.setTotalAmount(intialAmountInATM);
		
		atmMachineInstance = new ATMMachine();
		
		Account account1 = new Account();
		account1.setAccountNumber("123456789");
		account1.setPin("1234");
		account1.setbalance(800);
		account1.setOverdraftAmount(200);
		
		Account account2 = new Account();
		account2.setAccountNumber("987654321");
		account2.setPin("4321");
		account2.setbalance(1230);
		account2.setOverdraftAmount(150);
		
		Map<Integer,Integer> cashRegister = new TreeMap<Integer,Integer>(Collections.reverseOrder());
		
		cashRegister.put(50, 10);
		cashRegister.put(20, 30);
		cashRegister.put(10, 30);
		cashRegister.put(5, 20);
		
		Map<String,Account> accountMap = new TreeMap<String,Account>();
		
		accountMap.put(account1.getAccountNumber(), account1);
		accountMap.put(account2.getAccountNumber(), account2);
		
		atmMachineInstance.setAccountMap(accountMap);
		
		atmMachineInstance.setDenominationNumberMap(cashRegister);
		
		logger.info("ATM Initialization done.");
		logger.info("ATM INFO : "+atmMachineInstance);
			
	}
	
	public BalanceCheckResponse checkBalanceAmount(BalanceCheckRequest balanceCheckRequest) throws Exception {
		
		BalanceCheckResponse balanceCheckResponse = new BalanceCheckResponse();
		
		ErrorResponse errorResponse = preCheckRequest(balanceCheckRequest.accountNumber, balanceCheckRequest.pin, false, null);
		
		try {
			if(errorResponse!=null) {
				balanceCheckResponse.errorResponse = errorResponse;
				return balanceCheckResponse;
			}
			else {
				
				Account account = atmMachineInstance.getAccountMap().get(balanceCheckRequest.accountNumber);
				
				account.setAccountLocked(false);
				
				account.setRetryCount(0);
				
				balanceCheckResponse.accountNumber = account.getAccountNumber();
				
				balanceCheckResponse.balanceInAccount = String.valueOf(account.getbalance());
				
				double amountInATM = ATMMachine.getTotalAmount();
				
				double differenceAmount = amountInATM - (account.getbalance()  + account.getOverdraftAmount());
				
				if(amountInATM  <= 0 && differenceAmount<0) {
					balanceCheckResponse.maximumWithdrawableAmount = "Max withdrawable Amount not available";
				}
				else if (differenceAmount>0){
					balanceCheckResponse.maximumWithdrawableAmount = String.valueOf(account.getbalance()  + account.getOverdraftAmount());
				}
			  
				return balanceCheckResponse;
			}
		} catch (Exception e) {
			logger.error("Exception occurred inside checkBalanceAmount().");
			e.printStackTrace();
			throw new Exception("Technical Exception occurred.");
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public CashWithdrawalResponse withdrawAmount(CashWithdrawalRequest cashWithdrawRequest) throws Exception  {
		
		ErrorResponse errorResponse = preCheckRequest(cashWithdrawRequest.accountNumber, cashWithdrawRequest.pin, true, cashWithdrawRequest.amountToWithdraw);
		
		CashWithdrawalResponse withdrawalResponse = new CashWithdrawalResponse();
	
		try {
			if(errorResponse!=null) {
				
				withdrawalResponse.errorResponse = errorResponse;
				
				return withdrawalResponse;
				
			}
			else {
				
				Account account = atmMachineInstance.getAccountMap().get(cashWithdrawRequest.accountNumber);
				
			    account.setAccountLocked(false);
				
				account.setRetryCount(0);
				
				String cashDispensed = withdrawCash(account, cashWithdrawRequest.amountToWithdraw);
				
				withdrawalResponse.notesDispensed = cashDispensed;
				
				withdrawalResponse.remainingBalance = String.valueOf(account.getbalance());
				
				return withdrawalResponse;
			}
		} catch (Exception e) {
			logger.error("Exception occurred inside withdrawAmount() method");
			e.printStackTrace();
			throw new Exception("Technical Exception occurred");
		}	
	}
	

	private ErrorResponse preCheckRequest(String accountNumber, String pin, boolean isWithdrawalRequest, Double withdrawalAmount) {
		
		ErrorResponse errorResponse = null;
		
		Map<String,Account> accountMap = atmMachineInstance.getAccountMap();
		
		if(!(accountNumber!=null && !accountNumber.equals(""))) {
			errorResponse = new ErrorResponse(ERROR_CODE_ACCOUNT_NUMBER_NOT_PROVIDED,errorCodeMessageMap.get(ERROR_CODE_ACCOUNT_NUMBER_NOT_PROVIDED));
			return errorResponse;
		}
		
		if(!(pin!=null && !pin.equals(""))) {
			errorResponse = new ErrorResponse(ERROR_CODE_PIN_NOT_PROVIDED,errorCodeMessageMap.get(ERROR_CODE_PIN_NOT_PROVIDED));
			return errorResponse;	
		}
		
		Account account = accountMap.get(accountNumber);
		
		if(account!=null && account.isAccountLocked()) {
			errorResponse = new ErrorResponse(ERROR_CODE_CARD_LOCKED_OUT,errorCodeMessageMap.get(ERROR_CODE_CARD_LOCKED_OUT));
			return errorResponse;
		}
		
		if(!(account!=null && account.getPin().equals(pin))) {
			
			account.setRetryCount(account.getRetryCount()+1);
			
			account.setAccountLocked((account.getRetryCount()==MAX_PIN_RETRY_COUNT) ? true : false);
			
			String errorCode = (account.isAccountLocked()) ? ERROR_CODE_CARD_LOCKED_OUT : ERROR_CODE_INVALID_PIN;
			
			String attemptsRemaining = (account.getRetryCount() < MAX_PIN_RETRY_COUNT) ? MAX_PIN_RETRY_COUNT - account.getRetryCount()+" more attempts" : ""; 
			
			errorResponse = new ErrorResponse(errorCode,errorCodeMessageMap.get(errorCode)+attemptsRemaining);
			return errorResponse;
		}
		
		if(isWithdrawalRequest && !(withdrawalAmount!=null && withdrawalAmount.intValue()>0)) {
			errorResponse = new ErrorResponse(ERROR_CODE_INVALID_AMOUNT,errorCodeMessageMap.get(ERROR_CODE_INVALID_AMOUNT));
			return errorResponse;
		}
		
		if(isWithdrawalRequest && !(withdrawalAmount!=null && withdrawalAmount % 1==0)) {
			errorResponse = new ErrorResponse(ERROR_CODE_DENOMINATION_NOT_VALID,errorCodeMessageMap.get(ERROR_CODE_DENOMINATION_NOT_VALID));
			return errorResponse;
		}
		
		if(isWithdrawalRequest && !(withdrawalAmount.intValue() % 5==0 || withdrawalAmount.intValue() % 10==0)) {
			errorResponse = new ErrorResponse(ERROR_CODE_DENOMINATION_NOT_VALID,errorCodeMessageMap.get(ERROR_CODE_DENOMINATION_NOT_VALID));
			return errorResponse;
		}
		
		if(isWithdrawalRequest && !(withdrawalAmount.intValue()<=ATMMachine.getTotalAmount())) {
			errorResponse = new ErrorResponse(ERROR_CODE_INSUFFICIENT_FUND_IN_ATM,errorCodeMessageMap.get(ERROR_CODE_INSUFFICIENT_FUND_IN_ATM));
			return errorResponse;
		}
		
		if(isWithdrawalRequest && !(withdrawalAmount.intValue()<=account.getbalance()+account.getOverdraftAmount())) {
			errorResponse = new ErrorResponse(ERROR_CODE_INSUFFICIENT_BALANCE,errorCodeMessageMap.get(ERROR_CODE_INSUFFICIENT_BALANCE));
			return errorResponse;
		}
		
		
		return errorResponse;
		
	}
	
	private String withdrawCash(Account account, Double requestedAmount) {
		
		String cashDispense="";

		Map<Integer,Integer> notesDispenseMap = new TreeMap<Integer,Integer>(Collections.reverseOrder());

		Map<Integer,Integer> cashRegister = atmMachineInstance.getDenominationNumberMap();

		double amount = requestedAmount;

		for(Map.Entry<Integer, Integer> entrySet : cashRegister.entrySet()) {

			Integer denominationValue = entrySet.getKey();

			Integer denominationCount = entrySet.getValue();

			if(amount > 0 && amount / denominationValue >=1  && denominationCount>0 ){

				int notesCount = (int) amount / denominationValue;

				notesCount = (notesCount > denominationCount) ? denominationCount : notesCount;

				notesDispenseMap.put(denominationValue, notesCount);

				cashRegister.put(denominationValue, denominationCount-notesCount);

				amount = amount - (denominationValue*notesCount);

			}

		}

		List<String> cashDispenserList = new ArrayList<String>();

		for(Map.Entry<Integer,Integer> cashEntrySet : notesDispenseMap.entrySet()) {
			cashDispenserList.add(cashEntrySet.getKey()+"€ x "+cashEntrySet.getValue());
		}

		cashDispense = String.join(",", cashDispenserList);

		double amountInAccount = account.getbalance();

		double overDraft = account.getOverdraftAmount();

		double remainingBalance = amountInAccount - requestedAmount ;

		double remainingOverDraft  =  (remainingBalance < 0) ? overDraft - Math.abs(remainingBalance) : overDraft;

		remainingBalance = (remainingBalance < 0) ? 0 : remainingBalance;

		account.setbalance(remainingBalance);

		account.setOverdraftAmount(remainingOverDraft);

		ATMMachine.setTotalAmount(ATMMachine.getTotalAmount()-requestedAmount);

		atmMachineInstance.setDenominationNumberMap(cashRegister);	

		return cashDispense;

	}
	
}
