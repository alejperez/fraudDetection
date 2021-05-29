package edu.uoc.tfm.frauddetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Jose Perez
 * 
 * This application requires a running Neo4j database running 
 * according to parameters in application.properties file.
 * 
 */
@SpringBootApplication
public class FraudDetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectionApplication.class, args);
	}
}
