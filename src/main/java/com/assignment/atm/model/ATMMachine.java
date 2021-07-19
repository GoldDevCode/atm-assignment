package com.assignment.atm.model;

import java.util.Map;
import java.util.TreeMap;

public class ATMMachine {
	
	private static double totalAmount;
	
	private  Map<Integer,Integer> denominationNumberMap = new TreeMap<Integer,Integer>();
	
	private  Map<String,Account> accountMap = new TreeMap<String,Account>();
	
	public static double getTotalAmount() {
		return totalAmount;
	}

	public static void setTotalAmount(double totalAmount) {
		ATMMachine.totalAmount = totalAmount;
	}

	public Map<Integer, Integer> getDenominationNumberMap() {
		return denominationNumberMap;
	}

	public void setDenominationNumberMap(Map<Integer, Integer> denominationNumberMap) {
		this.denominationNumberMap = denominationNumberMap;
	}

	public Map<String, Account> getAccountMap() {
		return accountMap;
	}

	public void setAccountMap(Map<String, Account> accountMap) {
		this.accountMap = accountMap;
	}

	@Override
	public String toString() {
		return "ATMMachine [denominationNumberMap=" + denominationNumberMap + ", accountMap=" + accountMap + "]";
	}

}
