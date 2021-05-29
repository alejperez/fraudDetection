package edu.uoc.tfm.frauddetection.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.uoc.tfm.frauddetection.api.MerchantDO;
import edu.uoc.tfm.frauddetection.model.Merchant;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Jose Perez
 */
@RepositoryRestResource(path = "merchant")
@Tag(name = "merchant")
public interface MerchantRepository extends Neo4jRepository<Merchant, String> {

	List<Merchant> findAll();

	Optional<Merchant> findById(String id);

	List<Merchant> findByName(String name);

	List<Merchant> findByCategory(String category);

	List<Merchant> findByNameAndCategory(String name, String category);

	@Query("MATCH (m:Merchant{name: $name}) RETURN m")
	List<Merchant> findByName2(String name);

	@Query("MATCH (m:Merchant{id:$id}) OPTIONAL MATCH (m) -[:SOLD_BY] - (t:Transaction) RETURN m, collect(DISTINCT t) as sold\n")
	MerchantDO getMerchantTransactions(String id);

}
