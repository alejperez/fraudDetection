package edu.uoc.tfm.frauddetection.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uoc.tfm.frauddetection.model.Merchant;
import edu.uoc.tfm.frauddetection.repo.MerchantRepository;
import edu.uoc.tfm.frauddetection.repo.TransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * @author Jose Perez
 */
@RestController

@Tag(name = "merchants")
class MerchantController {

	private final MerchantRepository merchantRepository;

	MerchantController(MerchantRepository merchantRepository) {
		this.merchantRepository = merchantRepository;
	}
	
	
	@Operation(summary = "Get all merchants")
	@RequestMapping("/api/v0/merchants")
	List<Merchant> merchants() {
		return merchantRepository.findAll();
	}

	@Operation(summary = "Query Merchants by Name")
	@GetMapping("/getMerchantByName/{name}")
	List<Merchant> getDirectedBy(@PathVariable String name) {
		return merchantRepository.findByName(name);
	}
	
	@Operation(summary = "Add a new merchant")
	@PostMapping("/add")
	Merchant newMerchant(@RequestBody MerchantDO newMerchant) {
		Merchant merchant = new Merchant(newMerchant.getName(), newMerchant.getCategory());
		return merchantRepository.save(merchant);
	}

}
