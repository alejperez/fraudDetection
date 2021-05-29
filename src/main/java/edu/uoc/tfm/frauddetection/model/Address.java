package edu.uoc.tfm.frauddetection.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

/**
 * @author Jose Perez
 */
@Node
public class Address {

	/*
	 * MERGE (address:Address { id: toUpper(line.zip+"_"+line.city+"_"+line.street),
	 * street: line.street, city: line.city, zip : line.zip, state : line.state,
	 * lat: line.lat, lon : line.long}) MERGE (address)-[:USED_BY]-(customer)
	 * 
	 * CREATE (transaction)-[:DELIVERED_TO]->(address)
	 */

	public String getId() {
		return id;
	}

	@Id
	private String id;

	private String street;

	private String city;

	private String state;

	private String zip;

	private String lat;

	private String lon;

	@Relationship(type = "DELIVERED_TO", direction = Direction.INCOMING)
	private List<Transaction> transactions = new ArrayList<>();

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

	public Address(String street, String city, String state, String zip) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.id = composeAddressId(street, city, zip);
	}

	public static String composeAddressId(String street, String city, String zip) {
		return zip.toUpperCase() + "_" + city.toUpperCase() + "_" + street.toUpperCase();
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", city=" + city + ", state=" + state + ", zip=" + zip + "]";
	}

}
