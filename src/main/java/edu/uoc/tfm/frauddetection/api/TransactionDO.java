package edu.uoc.tfm.frauddetection.api;

import java.util.Date;

/**
 * @author Jose Perez
 */
public class TransactionDO {

	private String merchantName;
	private String category;
	private String customerFirstName;
	private String customerLastName; 
	private String customerGender;
	private String job;
	private String customerBirthDate;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String transactionNumber;
	private String cardNumber;
	private String amount;
	private String transactionTime;
	
	public TransactionDO(String merchantName, String category, String customerFirstName, String customerLastName,
			String customerGender, String job, String customerBirthDate, String street, String city, String state,
			String zip, String transactionNumber, String cardNumber, String amount, String transactionTime) {
		super();
		this.merchantName = merchantName;
		this.category = category;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.customerGender = customerGender;
		this.job = job;
		this.customerBirthDate = customerBirthDate;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.transactionNumber = transactionNumber;
		this.cardNumber = cardNumber;
		this.amount = amount;
		this.transactionTime = transactionTime;		
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerGender() {
		return customerGender;
	}

	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCustomerBirthDate() {
		return customerBirthDate;
	}

	public void setCustomerBirthDate(String customerBirthDate) {
		this.customerBirthDate = customerBirthDate;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
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
	
	
	
	
}
