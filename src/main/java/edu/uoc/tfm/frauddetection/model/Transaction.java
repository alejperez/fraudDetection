package edu.uoc.tfm.frauddetection.model;

import java.util.Date;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * @author Jose Perez
 */
@Node
public class Transaction {
	
	@Id	
	private String number;

	private String cardNumber;

	private String amount;

	private String transactionTime;
	
	private boolean validated;

	private boolean isFraud;

	public Transaction() {
		
	}

	public Transaction(String number, String cardNumber, String amount, String transactionTime) {
		this.number = number;
		this.cardNumber = cardNumber;
		this.amount = amount;
		this.transactionTime = transactionTime;
		this.isFraud = false;
		this.validated = false;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public boolean isFraud() {
		return isFraud;
	}

	public void setFraud(boolean isFraud) {
		this.isFraud = isFraud;
	}

	public String getNumber() {
		return number;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public Boolean getIsFraud() {
		return isFraud;
	}

	public void setIsFraud(Boolean isFraud) {
		this.isFraud = isFraud;
	}	

}
