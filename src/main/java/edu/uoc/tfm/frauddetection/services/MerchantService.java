	package edu.uoc.tfm.frauddetection.services;

import org.neo4j.driver.Driver;
import org.springframework.stereotype.Service;

import edu.uoc.tfm.frauddetection.model.Merchant;
import edu.uoc.tfm.frauddetection.repo.MerchantRepository;

/**
 * @author Jose Perez
 */
@Service
public class MerchantService {

	private final MerchantRepository merchantRepository;

	MerchantService(MerchantRepository merchantRepository, Driver driver) {
		this.merchantRepository = merchantRepository;
	}
	
	public void createMerchant(String name, String category) {
		
		Merchant merchant = new Merchant(name, category);
		
		merchantRepository.save(merchant);
	}
	
	

	
}
