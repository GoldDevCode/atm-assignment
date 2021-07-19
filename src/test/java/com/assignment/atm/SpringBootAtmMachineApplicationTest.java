package com.assignment.atm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.assignment.atm.dto.BalanceCheckRequest;
import com.assignment.atm.dto.BalanceCheckResponse;
import com.assignment.atm.dto.CashWithdrawalRequest;
import com.assignment.atm.dto.CashWithdrawalResponse;
import com.assignment.atm.service.ATMService;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(OrderAnnotation.class)
class SpringBootAtmMachineApplicationTest {
	
	@Autowired
	private ATMService atmService;
	
	@Test
	void contextLoads() {
		
	}
	
	@Before
	public void initializeATMForTest() {
		atmService.initializeATM();
	}
	
	@Order(1)
	@Test
	void test_CheckBalanceAccount() throws Exception {
		BalanceCheckRequest request = new BalanceCheckRequest();
		request.accountNumber = "123456789";
		request.pin="1234";
		BalanceCheckResponse response = atmService.checkBalanceAmount(request);
		assertEquals(null, response.errorResponse);
		assertNotEquals(null, response.balanceInAccount);
		assertEquals("800.0", response.balanceInAccount);
	}
	
	
	@Order(2)
	@Test
	void test_EmptyOrNullPin() throws Exception {
		BalanceCheckRequest request = new BalanceCheckRequest();
		request.accountNumber = "123456789";
		request.pin="";
		BalanceCheckResponse response = atmService.checkBalanceAmount(request);
		assertEquals(null, response.balanceInAccount);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2001", response.errorResponse.getErrorCode());
	}
	
	@Order(3)
	@Test
	void test_InvalidOrEmptyAccount() throws Exception {
		BalanceCheckRequest request = new BalanceCheckRequest();
		request.accountNumber = "";
		request.pin="1234";
		BalanceCheckResponse response = atmService.checkBalanceAmount(request);
		assertEquals(null, response.balanceInAccount);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2000", response.errorResponse.getErrorCode());
	}
	
	@Order(4)
	@Test
	void test_InvalidPin() throws Exception {
		BalanceCheckRequest request = new BalanceCheckRequest();
		request.accountNumber = "123456789";
		request.pin="111";
		BalanceCheckResponse response = atmService.checkBalanceAmount(request);
		assertEquals(null, response.balanceInAccount);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2002", response.errorResponse.getErrorCode());
	}
	
	@Order(5)
	@Test
	void test_InvalidEmptyOrNullAmount() throws Exception {
		CashWithdrawalRequest request = new CashWithdrawalRequest();
		request.accountNumber = "123456789";
		request.pin="1234";
		request.amountToWithdraw=null;
		CashWithdrawalResponse response = atmService.withdrawAmount(request);
		assertEquals(null, response.notesDispensed);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2003", response.errorResponse.getErrorCode());
	}
	
	@Order(6)
	@Test
	void test_InvalidDenominationAmount() throws Exception {
		CashWithdrawalRequest request = new CashWithdrawalRequest();
		request.accountNumber = "123456789";
		request.pin="1234";
		request.amountToWithdraw=127.0;
		CashWithdrawalResponse response = atmService.withdrawAmount(request);
		assertEquals(null, response.notesDispensed);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2007", response.errorResponse.getErrorCode());
	}
	
	@Order(7)
	@Test
	void test_InvalidDenominationAmountInDecimal() throws Exception {
		CashWithdrawalRequest request = new CashWithdrawalRequest();
		request.accountNumber = "123456789";
		request.pin="1234";
		request.amountToWithdraw=125.23;
		CashWithdrawalResponse response = atmService.withdrawAmount(request);
		assertEquals(null, response.notesDispensed);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2007", response.errorResponse.getErrorCode());
	}
	
	@Order(8)
	@Test
	void test_InsufficientBalanceInAccount() throws Exception {
		CashWithdrawalRequest request = new CashWithdrawalRequest();
		request.accountNumber = "123456789";
		request.pin="1234";
		request.amountToWithdraw=1200.0;
		CashWithdrawalResponse response = atmService.withdrawAmount(request);
		assertEquals(null, response.notesDispensed);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2004", response.errorResponse.getErrorCode());
	}
	
	@Order(9)
	@Test
	void test_InsufficientFundInATM() throws Exception {
		CashWithdrawalRequest request = new CashWithdrawalRequest();
		request.accountNumber = "123456789";
		request.pin="1234";
		request.amountToWithdraw=2000.0;
		CashWithdrawalResponse response = atmService.withdrawAmount(request);
		assertEquals(null, response.notesDispensed);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2005", response.errorResponse.getErrorCode());
	}
	
	@Order(10)
	@Test
	void test_WithdrawAmountSuccess() throws Exception {
		CashWithdrawalRequest request = new CashWithdrawalRequest();
		request.accountNumber = "123456789";
		request.pin="1234";
		request.amountToWithdraw=600.0;
		CashWithdrawalResponse response = atmService.withdrawAmount(request);
		assertEquals(null, response.errorResponse);
		assertNotEquals(null, response.notesDispensed);
		assertEquals("200.0", response.remainingBalance);
	}
	
	@Order(11)
	@Test
	void test_CardLockedOut() throws Exception {
		BalanceCheckRequest request = new BalanceCheckRequest();
		request.accountNumber = "123456789";
		request.pin="111";
		atmService.checkBalanceAmount(request);
		atmService.checkBalanceAmount(request);
		BalanceCheckResponse response = atmService.checkBalanceAmount(request);
		assertEquals(null, response.balanceInAccount);
		assertNotEquals(null, response.errorResponse);
		assertEquals("2006", response.errorResponse.getErrorCode());
	}
	
}
