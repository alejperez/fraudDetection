	package edu.uoc.tfm.frauddetection.services;

import java.util.Date;

import org.neo4j.driver.Driver;
import org.springframework.stereotype.Service;

import edu.uoc.tfm.frauddetection.model.Customer;
import edu.uoc.tfm.frauddetection.repo.CustomerRepository;

/**
 * @author Jose Perez
 */
@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	CustomerService(CustomerRepository customerRepository, Driver driver) {
		this.customerRepository = customerRepository;
	}
	
	public void createMerchant(String firstName, String lastName, String gender,  String job, Date birthDate) {
		
		Customer customer = new Customer(firstName, lastName, job, gender, birthDate);
		
		customerRepository.save(customer);
	}
	
	

	
}
