package edu.uoc.tfm.frauddetection.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * @author Jose Perez
 */
@Node
public class Merchant {

	/*
	MERGE (merchant:Merchant { id: toUpper(substring(line.merchant, 6)), name: substring(line.merchant, 6), 
			category: line.category})
			
	CREATE (transaction)-[:SOLD_BY]->(merchant)
				
	*/	
	
	@Id
	private String id;

	private String name;

	private String category;
	  
	@Relationship(type = "SOLD_BY")
	public List<Transaction> transactions;
	
	public Merchant(String id, String name, String category) {
		this.id = id;
		this.category = category;
		this.name = name;
	}
	
	public Merchant( ) {}

	public Merchant(String name, String category) {
		this.id = composeMerchantId(name, category);
		this.category = category;
		this.name = name;
	}
	
	
	public Merchant(String id, String name, String category, List<Transaction> transactions) {
		this.id = id;
		this.category = category;
		this.name = name;
		this.transactions = transactions;
	}
	
	public static String composeMerchantId(String name, String category) {
		return name.toUpperCase()+"_"+category.toUpperCase();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	@Override
	public String toString() {
		return "Merchant [name=" + name + ", category=" + category + "]";
	}



}
