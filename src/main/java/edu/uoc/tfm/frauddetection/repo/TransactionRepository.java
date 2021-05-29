package edu.uoc.tfm.frauddetection.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.uoc.tfm.frauddetection.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Jose Perez
 */
@RepositoryRestResource(path = "transaction")
@Tag(name = "transaction")
public interface TransactionRepository extends Neo4jRepository<Transaction, String> {

	List<Transaction> findAll();

	Optional<Transaction> findByNumber(String number);
	
	List<Transaction> findByCardNumber(String cardNumber);

	@Operation(summary = "Get transactions by Merchant id")
	@Query("MATCH (m:Merchant{id: $id}) -[:SOLD_BY] - (t:Transaction) RETURN DISTINCT t")
	List<Transaction> getMerchantTransactionsById(String id);

	@Operation(summary = "Get transactions by Merchant name and category")	
	@Query("MATCH (m:Merchant{name: $name, category: $category}) -[:SOLD_BY] - (t:Transaction) RETURN DISTINCT t")	
	List<Transaction> getMerchantTransactions(String name, String category);
	
	
	@Query("MATCH (t:Transaction{number: $number}) RETURN t")
	List<Transaction> getByNumber(String number);
	
	
	@Query("MATCH (t:Transaction) WHERE t.number =~ ('(?i).*'+{number}+'.*') RETURN t")
	List<Transaction> findByApproxNumber(String number);
		
}
	