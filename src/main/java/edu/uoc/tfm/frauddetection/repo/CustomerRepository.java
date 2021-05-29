package edu.uoc.tfm.frauddetection.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.uoc.tfm.frauddetection.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Jose Perez
 */
@RepositoryRestResource(path = "customer")
@Tag(name = "customer")
public interface CustomerRepository extends Neo4jRepository<Customer, String> {

	@Operation(summary = "Retrieve all customers")
	List<Customer> findAll();

	@Operation(summary = "Get customer by id")	
	Optional<Customer> findById(String id);

	@Operation(summary = "Find customer by first name and last name")	
	List<Customer> findByFirstNameAndLastName(String firstName, String lastName);

	
}
