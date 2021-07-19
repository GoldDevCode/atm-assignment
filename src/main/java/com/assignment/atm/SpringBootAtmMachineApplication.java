package com.assignment.atm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.assignment.atm.service.ATMService;

@SpringBootApplication
public class SpringBootAtmMachineApplication {

	@Autowired
	private ATMService atmService;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootAtmMachineApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {	
		atmService.initializeATM();
	}

}
