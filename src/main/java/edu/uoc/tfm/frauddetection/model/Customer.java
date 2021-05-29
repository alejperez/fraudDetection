package edu.uoc.tfm.frauddetection.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

/**
 * @author Jose Perez
 */
@Node
public class Customer {

	/*
	 * MERGE (customer:Customer { id:
	 * toUpper(line.last+"_"+line.first+"_"+line.job), firstName: line.first,
	 * lastName: line.last, job: line.job, gender: line.gender, job: line.job,
	 * birthDate : date(line.dob)})
	 * 
	 * CREATE (transaction)-[:PURCHASED_BY]->(customer);
	 */

	@Id
	private String id;

	private String firstName;

	private String lastName;

	private String job;

	private String gender;

	private Date birthDate;

	@Relationship(type = "USED_BY")
	private List<Address> addresses;

	@Relationship(type = "PURCHASED_BY")
	private List<Transaction> transactions;

	public Customer(String firstName, String lastName, String job, String gender, Date birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.job = job;
		this.gender = gender;
		this.birthDate = birthDate;
		this.id = composeCustomerId(firstName, lastName, job);
	}

	public static String composeCustomerId(String firstName, String lastName, String job) {
		if (StringUtils.isNotBlank(job))
			return (lastName.toUpperCase() + "_" + firstName.toUpperCase() + "_" + job.toUpperCase());
		else
			return (lastName.toUpperCase() + "_" + firstName.toUpperCase());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void addTransaction(Transaction transaction) {
		if (this.transactions == null)
			this.transactions = new ArrayList<Transaction>();
		transactions.add(transaction);
	}

	public void addAddress(Address address) {
		if (this.addresses == null)
			this.addresses = new ArrayList<Address>();
		addresses.add(address);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", job=" + job
				+ ", gender=" + gender + ", birthDate=" + birthDate + "]";
	}

}
