package com.assignment.atm.model;

public class Account {
	
	private String accountNumber;
	
	private String pin;
	
	private double balance;
	
	private double overdraftAmount;
	
	private int retryCount;
	
	private boolean isAccountLocked;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public double getbalance() {
		return balance;
	}

	public void setbalance(double balance) {
		this.balance = balance;
	}

	public double getOverdraftAmount() {
		return overdraftAmount;
	}

	public void setOverdraftAmount(double overdraftAmount) {
		this.overdraftAmount = overdraftAmount;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isAccountLocked() {
		return isAccountLocked;
	}

	public void setAccountLocked(boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", pin=" + pin + ", balance=" + balance
				+ ", overdraftAmount=" + overdraftAmount + "]";
	}
	
	

}
