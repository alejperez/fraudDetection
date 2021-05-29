package edu.uoc.tfm.frauddetection.api;

import java.util.List;

import edu.uoc.tfm.frauddetection.model.Transaction;

/**
 * @author Jose Perez
 */
public class MerchantDO {

	private String name;

	private String category;
	
	private List<Transaction> sold;

	public MerchantDO() {
		
	}

	public MerchantDO(String name, String category) {
		this.category = category;
		this.name = name;
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

	public MerchantDO(String name, String category, List<Transaction> sold) {
		this.name = name;
		this.category = category;
		this.sold = sold;
	}

	public List<Transaction> getSold() {
		return sold;
	}

	public void setSold(List<Transaction> sold) {
		this.sold = sold;
	}

	@Override
	public String toString() {
		return "Merchant [name=" + name + ", category=" + category + "]";
	}

}
