	package edu.uoc.tfm.frauddetection.api;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uoc.tfm.frauddetection.model.Merchant;
import edu.uoc.tfm.frauddetection.model.Transaction;
import edu.uoc.tfm.frauddetection.repo.CustomerRepository;
import edu.uoc.tfm.frauddetection.repo.MerchantRepository;
import edu.uoc.tfm.frauddetection.repo.TransactionRepository;
import edu.uoc.tfm.frauddetection.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Jose Perez
 */
@RestController

@Tag(name = "transactions")
class TransactionController {

	private final CustomerRepository customerRepository;
	private final MerchantRepository merchantRepository;
	private final TransactionService transactionService;
	private final TransactionRepository transactionRepository;

	public TransactionController(CustomerRepository customerRepository, MerchantRepository merchantRepository,
			TransactionService transactionService, TransactionRepository transactionRepository) {

		this.customerRepository = customerRepository;
		this.merchantRepository = merchantRepository;
		this.transactionService = transactionService;
		this.transactionRepository = transactionRepository;
	}
	
	@Operation(summary = "Get all transactions")
	@RequestMapping("/api/v0/transactions")
	List<Transaction> transactions() {
		return transactionRepository.findAll();
	}

	@Operation(summary = "Validate input transaction. Not saved.")	
	@PostMapping("/validate")
	List<ProcessingResult> validateTransaction(@RequestBody TransactionDO newTransaction) {
		return transactionService.validateTransaction(newTransaction);
	}

	@Operation(summary = "Save transaction, no validation")	
	@PostMapping("/create")
	String createTransaction(@RequestBody TransactionDO newTransaction) {
		Transaction transaction = transactionService.createTransaction(newTransaction);
		return transaction.getNumber();
	}

	@Operation(summary = "Save and validate input transaction")
	@PostMapping("/process")
	List<ProcessingResult> processTransaction(@RequestBody TransactionDO newTransaction) {
		return transactionService.processTransaction(newTransaction);
	}

	@Operation(summary = "Validate an already saved transaction by its number")
	@PutMapping("/process_by_id/{number}")
	List<ProcessingResult> reprocessTransaction(@PathVariable String number) {
		return transactionService.reprocessTransaction(number);
	}

	@Operation(summary = "Get all transactions for given Merchant")
	@GetMapping("/getMerchantTransactions/{id}")
	List<Transaction> getMerchantTransactions(@PathVariable String id) {
		List<Transaction> result = transactionRepository.getMerchantTransactionsById(id);		
		return result;
	}
	
	@Operation(summary = "Retrieve transactions by aproximated number")
	@GetMapping("/find/{number}")
	List<Transaction> getById(@PathVariable String number) {
		List<Transaction> result = transactionRepository.findByApproxNumber(number);		
		return result;
	}
	
	@Operation(summary = "Retrieve transaction by number")
	@GetMapping("/get/{number}")
	Optional<Transaction> findById(@PathVariable String number) {
		Optional<Transaction> result = transactionRepository.findByNumber(number);		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
