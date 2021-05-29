package edu.uoc.tfm.frauddetection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import edu.uoc.tfm.frauddetection.api.TransactionDO;
import edu.uoc.tfm.frauddetection.api.ProcessingResult;
import edu.uoc.tfm.frauddetection.model.Customer;
import edu.uoc.tfm.frauddetection.model.Merchant;
import edu.uoc.tfm.frauddetection.model.Transaction;
import edu.uoc.tfm.frauddetection.repo.CustomerRepository;
import edu.uoc.tfm.frauddetection.repo.TransactionRepository;
import edu.uoc.tfm.frauddetection.services.TransactionService;

/**
 * @author Jose Perez
 * 
 * Transaction Service  and repository tests
 * 
 */
@ComponentScan("edu.uoc.tfm.frauddetection.services")
@DataNeo4jTest
public class TransactionServiceTest {

	private static Neo4j embeddedDatabaseServer;

	@DynamicPropertySource
	static void neo4jProperties(DynamicPropertyRegistry registry) {

		registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
		registry.add("spring.neo4j.authentication.username", () -> "neo4j");
		registry.add("spring.neo4j.authentication.password", () -> null);
	}

	@BeforeAll
	static void initializeNeo4j() {

		embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder().withDisabledServer() // Don't need Neos HTTP server
				.withFixture(""
						/*
						 * +
						 * "CREATE CONSTRAINT unique_customer IF NOT EXISTS ON (c:Customer) ASSERT c.id IS UNIQUE\n"
						 * + "CREATE CONSTRAINT unique_merchant ON (m:Merchant) ASSERT m.id IS UNIQUE\n"
						 * +
						 * "CREATE CONSTRAINT unique_address  IF NOT EXISTS ON (a:Address) ASSERT a.id IS UNIQUE\n"
						 * +
						 * "CREATE CONSTRAINT unique_transaction  IF NOT EXISTS ON (t:Transaction) ASSERT t.number IS UNIQUE\n"
						 */
						+ "CREATE (EIRE_JANE:Customer {id: 'EIRE_JANE', firstName: 'Jane', lastName: 'Eire', job: 'accountant',gender: 'F'})\n"
						+ "CREATE (BARLOF_MICHAEL:Customer {id: 'BARLOF_MICHAEL',  firstName: 'Michael', lastName: 'Barlof', job: 'painter',gender: 'M'})\n"
						+ "CREATE (DANGLING_JOHN:Customer {id: 'DANGLING_JOHN', firstName: 'John', lastName: 'Dangling', job: 'firefighter',gender: 'M'})\n"
						+ "CREATE (DOMBASS_RAMMONA:Customer {id: 'DOMBASS_RAMMONA', firstName: 'Rammona', lastName: 'Dombass', job: 'librarian',gender: 'F'})\n"
						+ "CREATE (WYKOFF_ARNAUD:Customer {id: 'WYKOFF_ARNAUD', firstName: 'Arnaud', lastName: 'Wykoff', job: 'trader',gender: 'M'})\n"
						+ "CREATE (IBARRURI_DOLORES:Customer {id: 'IBARRURI_DOLORES',  firstName: 'Dolores', lastName: 'Ibarruri', job: 'polititian',gender: 'M'})\n"
						+ "CREATE (ALDRIDGE_LAMARKUS:Customer {id: 'ALDRIDGE_LAMARKUS',  firstName: 'LaMarcus', lastName: 'Aldridge', job: 'basketball',gender: 'M'})\n"
						+ "CREATE (VILANOVA_ANNE:Customer {id: 'VILANOVA_ANNE',  firstName: 'Anne', lastName: 'Vilanova', job: 'teacher',gender: 'F'})\n"
						+ "CREATE (MCBEAL_ALLY:Customer {id: 'MCBEAL_ALLY', firstName: 'Ally', lastName: 'McBeal', job: 'lawyer',gender: 'F'})")
				.build();

	}

	/**
	 * Create transaction and assert queries
	 * @param transactionService
	 * @param transactionRepo
	 */
	@Test
	public void createTransaction(@Autowired TransactionService transactionService,
			@Autowired TransactionRepository transactionRepo) {

		/*
		 * trans_date_trans_time cc_num merchant category amt first last gender street
		 * city state zip lat long city_pop job dob trans_num unix_time merch_lat
		 * merch_long is_fraud 0 6/21/2020 12:14 2.29E+15 fraud_Kirlin and Sons
		 * personal_care 2.86 Jeff Elliott M 351 Darlene Green Columbia SC 29209 33.9659
		 * -80.9355 333497 Mechanical engineer 3/19/1968
		 * 2da90c7d74bd46a0caf3777415b3ebd3 1371816865 33.986391 -81.200714 0
		 */

		String merchantName = "Kirlin and Sons";
		String merchantCategory = "personal_care";

		TransactionDO input = new TransactionDO(merchantName, merchantCategory, "Jeff", "Elliott", "M",
				"Mechanical engineer", "3/19/1968", "351 Darlene Green", "Columbia", "SC", "29209",
				"2da90c7d74bd46a0caf3777415b3ebd3", "6538440000000000", "2.86", String.valueOf(new Date().getTime()));

		Transaction createdTransaction = transactionService.createTransaction(input);

		assertNotNull(createdTransaction);

		List<Transaction> transactions = transactionRepo.findAll();

		assertEquals(1, transactions.size());

		Optional<Transaction> transaction = transactionRepo.findById(createdTransaction.getNumber());

		assertNotNull(transaction.get());

		String merchantId = Merchant.composeMerchantId(merchantName, merchantCategory);

		List<Transaction> transactions2 = transactionRepo.getMerchantTransactionsById(merchantId);

		assertEquals(1, transactions2.size());

		List<Transaction> transactions3 = transactionRepo.getMerchantTransactions(merchantName, merchantCategory);

		assertEquals(1, transactions3.size());

	}

	/**
	 * Create and preform transaction validations
	 * @param transactionService
	 * @param transactionRepo
	 * @throws InterruptedException
	 */
	@Test
	public void processTransaction(@Autowired TransactionService transactionService,
			@Autowired TransactionRepository transactionRepo) throws InterruptedException {

		/*
		 * trans_date_trans_time cc_num merchant category amt first last gender street
		 * city state zip lat long city_pop job dob trans_num unix_time merch_lat
		 * merch_long is_fraud 0 6/21/2020 12:14 2.29E+15 fraud_Kirlin and Sons
		 * personal_care 2.86 Jeff Elliott M 351 Darlene Green Columbia SC 29209 33.9659
		 * -80.9355 333497 Mechanical engineer 3/19/1968
		 * 2da90c7d74bd46a0caf3777415b3ebd3 1371816865 33.986391 -81.200714 0
		 */

		String merchantName = "Kirlin and Sons";
		String merchantCategory = "personal_care";

		long now = (new Date()).getTime();

		TransactionDO input1 = new TransactionDO(merchantName, merchantCategory, "Jeff", "Elliott", "M",
				"Mechanical engineer", "3/19/1968", "351 Darlene Green", "Columbia", "SC", "29209", "1",
				"6538440000000000", "2.86", String.valueOf(now));

		Transaction createdTransaction1 = transactionService.createTransaction(input1);

		assertNotNull(createdTransaction1);
		
		createdTransaction1.setIsFraud(false);
		createdTransaction1.setValidated(true);

		TransactionDO input = new TransactionDO(merchantName, merchantCategory, "Jeff", "Elliott", "M",
				"Mechanical engineer", "3/19/1968", "351 Darlene Green", "Columbia", "SC", "29209", "2",
				"6538440000000000", "2.86", String.valueOf(new Date().getTime()));

		List<ProcessingResult> results = transactionService.validateTransaction(input);

		ProcessingResult result = results.get(0);

		assertEquals(ProcessingResult.REPEATED_PURCHASES_SHORT_TIME, result);
		
		
		input = new TransactionDO(merchantName, merchantCategory, "Jeff", "Smith", "M",
				"Mechanical engineer", "3/19/1968", "351 Darlene Green", "Columbia", "SC", "29209", "2",
				"6538440000000000", "2.86", String.valueOf(new Date().getTime()));

		results = transactionService.validateTransaction(input);

		result = results.get(0);

		assertEquals(ProcessingResult.REPEATED_PURCHASES_SHORT_TIME, result);		

		// 2 minutes: 2 * 60000

		long newTime = now + (120000);

		input = new TransactionDO(merchantName, merchantCategory, "Jeff", "Elliott", "M", "Mechanical engineer",
				"3/19/1968", "351 Darlene Green", "Columbia", "SC", "29209", "2", "6538440000000000", "2.86",
				String.valueOf(newTime));

		results = transactionService.validateTransaction(input);

		result = results.get(0);

		assertNotNull(result);

		results = transactionService.processTransaction(input);

		result = results.get(0);

		assertNotNull(result);
		
		Optional<Transaction> trans = transactionRepo.findById(input.getTransactionNumber());
		
		assertNotNull(trans.get());
		
	}

	@Test
	public void findById(@Autowired CustomerRepository customerRepository) {

		Optional<Customer> person = customerRepository.findById("EIRE_JANE");
		Customer customer = person.get();
		assertNotNull(customer);
	}

	@AfterAll
	static void stopNeo4j() {

		embeddedDatabaseServer.close();
	}
}