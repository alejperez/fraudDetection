package edu.uoc.tfm.frauddetection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import edu.uoc.tfm.frauddetection.model.Customer;
import edu.uoc.tfm.frauddetection.repo.CustomerRepository;


/**
 * @author Jose Perez
 */
@DataNeo4jTest
public class CustomerRepositoryTest {

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

	@Test
	public void findAllThatActedInMovieShouldWork(@Autowired CustomerRepository customerRepository) {
		assertThat(customerRepository.findByFirstNameAndLastName("John", "Dangling")).hasSize(1);
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