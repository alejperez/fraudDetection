package edu.uoc.tfm.frauddetection.api;

public enum ProcessingResult {
	
	
	TRANSACTION_OK("OK"),
	DIFFERENT_PEOPLE_SAME_CARD("Different people used the same card"),
	DIFFERENT_DELIVERY_ADDRESS("Different address used"),
	ADDRESS_FAR_FROM_PREVIOUS("Delivery address far from previous deliveries"),
	REPEATED_PURCHASES_SHORT_TIME("Many purchases with same card in short time"), 
	NEW_CREDIT_CARD("New credit card"), 
	ACCEPTED_CUSTOMER_CARD("Accepted customer card"), 
	OTHER_CUSTOMERS_CARD("Card used by another customer"), 
	NEW_CUSTOMER("New customer"), 
	NEW_ADDRESS("New address"), 
	ALREADY_USED_CUSTOMER_ADDRESS("Already used customer address"), 
	NOT_USED_CUSTOMER_ADDRESS("Not used customer address");
	
    public final String label;

    private ProcessingResult(String label) {
        this.label = label;
    }
	

}
