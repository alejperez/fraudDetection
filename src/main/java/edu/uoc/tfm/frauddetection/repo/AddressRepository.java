package edu.uoc.tfm.frauddetection.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.uoc.tfm.frauddetection.model.Address;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Jose Perez
 */
@RepositoryRestResource(path = "address")
@Tag(name = "address")
public interface AddressRepository extends Neo4jRepository<Address, String> {

	List<Address> findAll();

	Optional<Address> findById(String id);

	List<Address> findByStreet(String street, String zip);
	
	List<Address> findByStreetAndZip(String street, String zip);
	
	List<Address> findByStreetAndCityAndState(String street, String city, String state);

}
