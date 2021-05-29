package edu.uoc.tfm.frauddetection.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import edu.uoc.tfm.frauddetection.api.TransactionDO;
import edu.uoc.tfm.frauddetection.api.ProcessingResult;
import edu.uoc.tfm.frauddetection.model.Address;
import edu.uoc.tfm.frauddetection.model.Customer;
import edu.uoc.tfm.frauddetection.model.Merchant;
import edu.uoc.tfm.frauddetection.model.Transaction;
import edu.uoc.tfm.frauddetection.repo.AddressRepository;
import edu.uoc.tfm.frauddetection.repo.CustomerRepository;
import edu.uoc.tfm.frauddetection.repo.MerchantRepository;
import edu.uoc.tfm.frauddetection.repo.TransactionRepository;

/**
 * @author Jose Perez
 */

@Service
@Component
public class TransactionService {

	private final CustomerRepository customerRepository;
	private final MerchantRepository merchantRepository;
	private final AddressRepository addressRepository;
	private final TransactionRepository transactionRepository;

	private final Driver driver;

	public TransactionService(CustomerRepository customerRepository, MerchantRepository merchantRepository,
			AddressRepository addressRepository, TransactionRepository transactionRepository, Driver driver) {
		super();
		this.customerRepository = customerRepository;
		this.merchantRepository = merchantRepository;
		this.addressRepository = addressRepository;
		this.transactionRepository = transactionRepository;
		this.driver = driver;
	}

	public List<Transaction> getPurchasedBy(String name) {
		return transactionRepository.findByCardNumber(name);
	}

	/**
	 * Check issues in transaction
	 * @param newTransaction
	 * @return
	 */
	public List<ProcessingResult> validateTransaction(TransactionDO newTransaction) {

		List<ProcessingResult> results = new ArrayList<>();

		String customerId = Customer.composeCustomerId(newTransaction.getCustomerFirstName(),
				newTransaction.getCustomerLastName(), newTransaction.getJob());

		Optional<Customer> customer = customerRepository.findById(customerId);

		// Assert less than 2 minutes
		verifyTimeCreditCardUsage(newTransaction.getCardNumber(), Long.valueOf(newTransaction.getTransactionTime()), 2, results);

		// Assert credit card only used by this customer
		verifyCreditCardCustomer(customer, newTransaction.getCardNumber(), results);

		String addressId = Address.composeAddressId(newTransaction.getStreet(), newTransaction.getCity(),
				newTransaction.getZip());

		Optional<Address> address = addressRepository.findById(addressId);

		// Verify address used previously by customer in valid transaction
		verifyCustomerAddress(customer, address, results);

		return results;

	}

	/**
	 * Check if new transaction is too close to previous one with same credit card
	 * 
	 * @param cardNumber
	 * @param newTransactionTime
	 * @param minimumTimespan
	 * @param results
	 */
	private void verifyTimeCreditCardUsage(String cardNumber, Long newTransactionTime, long minimumTimespan,
			List<ProcessingResult> results) {

		List<Transaction> transactions = transactionRepository.findByCardNumber(cardNumber);

		for (Transaction transaction : transactions) {

			// Date of transaction in unix format
			Long transTime = Long.valueOf(transaction.getTransactionTime());

			// Get timespan between transaction in minutes
			long diffTime = getDateDiff(transTime, newTransactionTime, TimeUnit.MINUTES);

			// If timespan less than required period return error code
			if (diffTime < minimumTimespan) {
				results.add(ProcessingResult.REPEATED_PURCHASES_SHORT_TIME);
				return;
			}
		}
	}

	/**
	 * Validates if provided address has been used by this or any customer
	 * 
	 * @param customer
	 * @param address
	 * @param results
	 */
	private void verifyCustomerAddress(Optional<Customer> customer, Optional<Address> optionalAddress,
			List<ProcessingResult> results) {
		
		if (customer.isEmpty())			
			results.add(ProcessingResult.NEW_CUSTOMER);
		
		else {
			
			if (optionalAddress.isEmpty()) {
				results.add(ProcessingResult.NEW_ADDRESS);				
			} else {				
			
				List<Address> addresses = customer.get().getAddresses();
				
				Address address = optionalAddress.get();
				
				for (Address customerAddress : addresses) {					
					if (address.getId().equals(customerAddress.getId())) {						
						results.add(ProcessingResult.ALREADY_USED_CUSTOMER_ADDRESS);
						return;						
					}
				}
				
				results.add(ProcessingResult.NOT_USED_CUSTOMER_ADDRESS);
			}
				
		}		

	}

	/**
	 * Validates if this credit card number has been used by this customer
	 * 
	 * @param customer
	 * @param cardNumber
	 * @param results
	 */
	private void verifyCreditCardCustomer(Optional<Customer> customer, String cardNumber,
			List<ProcessingResult> results) {

		List<Transaction> transactions = transactionRepository.findByCardNumber(cardNumber);

		if (transactions.size() == 0) {
			results.add(ProcessingResult.NEW_CREDIT_CARD);
		}

		else {

			if (customer.isEmpty()) {
				
				results.add(ProcessingResult.OTHER_CUSTOMERS_CARD);
				
			} else {

				List<Transaction> customerTransactions = customer.get().getTransactions();

				for (Transaction transaction : customerTransactions) {

					if (transaction.isValidated() && !transaction.isFraud() && transaction.getCardNumber().equals(cardNumber)) {
						results.add(ProcessingResult.ACCEPTED_CUSTOMER_CARD);
						return;
					}
				}

				results.add(ProcessingResult.OTHER_CUSTOMERS_CARD);

			}

		}

	}

	/**
	 * Create Transaction based on InputTransaction payload
	 * 
	 * @param newTransaction
	 * @return
	 */
	public Transaction createTransaction(TransactionDO newTransaction) {

		return createTransaction(newTransaction.getMerchantName(), newTransaction.getCategory(),
				newTransaction.getCustomerFirstName(), newTransaction.getCustomerLastName(),
				newTransaction.getCustomerGender(), newTransaction.getJob(), null, newTransaction.getStreet(),
				newTransaction.getCity(), newTransaction.getState(), newTransaction.getZip(),
				newTransaction.getTransactionNumber(), newTransaction.getCardNumber(), newTransaction.getAmount().toString(),
				newTransaction.getTransactionTime());
	}

	/**
	 * Create Transaction with validation and isFraud flags set to false
	 * 
	 * @param merchantName
	 * @param category
	 * @param customerFirstName
	 * @param customerLastName
	 * @param customerGender
	 * @param job
	 * @param customerBirthDate
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param transactionNumber
	 * @param cardNumber
	 * @param amount
	 * @param transactionTime
	 * @return
	 */
	public Transaction createTransaction(String merchantName, String category, String customerFirstName,
			String customerLastName, String customerGender, String job, Date customerBirthDate, String street,
			String city, String state, String zip, String transactionNumber, String cardNumber, String amount,
			String transactionTime) {

		Merchant merchant = null;
		Customer customer = null;
		Address address = null;

		List<Merchant> merchants = merchantRepository.findByNameAndCategory(merchantName, category);

		if (merchants.size() == 0) {
			merchant = new Merchant(merchantName, category);
		} else
			merchant = merchants.get(0);

		String customerId = Customer.composeCustomerId(customerFirstName, customerLastName, job);

		Optional<Customer> findCustomer = customerRepository.findById(customerId);

		if (findCustomer.isEmpty()) {
			customer = new Customer(customerFirstName, customerLastName, job, customerGender,
					customerBirthDate);			
		} else {
			customer = findCustomer.get();
		}

		String addressId = Address.composeAddressId(street, city, zip);

		Optional<Address> findAddress = addressRepository.findById(addressId);

		if (findAddress.isEmpty()) {
			address = new Address(street, city, state, zip);
		} else {
			address = findAddress.get();
		}

		Transaction transaction = new Transaction(transactionNumber, cardNumber, amount, transactionTime);

		address.addTransaction(transaction);
		merchant.addTransaction(transaction);
		customer.addTransaction(transaction);
		customer.addAddress(address);

		customerRepository.save(customer);
		merchantRepository.save(merchant);
		addressRepository.save(address);
		transactionRepository.save(transaction);

		return transaction;

	}

	
	/**
	 * Validates and previously saved transaction. 
	 * Compute processing results
	 * 
	 * @param id
	 * @return
	 */
	public List<ProcessingResult> reprocessTransaction(String id) {
		
		
		
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Validate and save transaction according to result of validation
	 * Criteria < 2 issues, validated, fraud false
	 * > 2 validated, fraud true
	 * 3 not validated, for manual review
	 * @param newTransaction
	 * @return
	 */
	public List<ProcessingResult> processTransaction(TransactionDO newTransaction) {

		List<ProcessingResult> results = validateTransaction(newTransaction);
		
		Transaction createdTransaction = createTransaction(newTransaction);
		
		if (results.size()<2) {
			createdTransaction.setValidated(true);
			createdTransaction.setIsFraud(false);
		}
		
		if (results.size() > 3) {
			createdTransaction.setValidated(true);
			createdTransaction.setIsFraud(true);
		}
		
		return results;
	}

	/**
	 * Get a diff between two dates
	 * 
	 * @param oldDate  the oldest date
	 * @param newDate  the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	private long getDateDiff(Long oldDate, Long newDate, TimeUnit timeUnit) {
		long diffInMillies = newDate - oldDate;
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

}
