package edu.uoc.tfm.frauddetection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import edu.uoc.tfm.frauddetection.model.Merchant;
import edu.uoc.tfm.frauddetection.repo.MerchantRepository;

/**
 * @author Jose Perez
 */
@DataNeo4jTest
public class MerchantRepositoryTest {

	private static Neo4j embeddedDatabaseServer;

	@DynamicPropertySource
	static void neo4jProperties(DynamicPropertyRegistry registry) {

		registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
		registry.add("spring.neo4j.authentication.username", () -> "neo4j");
		registry.add("spring.neo4j.authentication.password", () -> null);
	}


	@BeforeAll
	static void initializeNeo4j() {

		embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
			.withDisabledServer() // Don't need Neos HTTP server
			.withFixture(""
				+ "CREATE (M1:Merchant { name: 'Merchant 1', category: 'BOOKS'})\n"
				+ "CREATE (M2:Merchant { name: 'Merchant 2', category: 'CARS'})\n"
				+ "CREATE (M3:Merchant { name: 'Merchant 3', category: 'TRAVELS'})\n"
				+ "CREATE (M4:Merchant { name: 'Merchant 4', category: 'COLLECTORS'})\n"
				+ "CREATE (M5:Merchant { name: 'Merchant 5', category: 'CARS'})\n"				
				+ "CREATE (M6:Merchant { name: 'Merchant 6', category: 'ASIAN'})\n"
			)
			.build();
		
	}

	@Test
	public void findAllThatActedInMovieShouldWork(@Autowired MerchantRepository merchantRepository) {

		assertThat(merchantRepository.findByCategory("CARS")).hasSize(2);
	}

	@Test
	public void getByNameShouldWork(@Autowired MerchantRepository merchantRepository) {
		assertThat(merchantRepository.findByName("Merchant 1")).hasSize(1);

	}
	
	
	@Test
	public void saveMerchant(@Autowired MerchantRepository merchantRepository) {
		
		Merchant merchant = new Merchant("Pepe", "Cuchillero");
		
		merchantRepository.save(merchant);

		assertThat(merchantRepository.findByCategory("Cuchillero")).hasSize(1);
		
		assertThat(merchantRepository.findByName("Pepe")).hasSize(1);
		
		List<Merchant> merchantList = merchantRepository.findByName2("Pepe");
		
		assertThat(merchantList).hasSize(1);
		
		assertThat(merchantRepository.findById("PEPE_CUCHILLERO")).isNotEmpty();
		
		
	}
	

	
	@AfterAll
	static void stopNeo4j() {

		embeddedDatabaseServer.close();
	}
}