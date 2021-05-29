package edu.uoc.tfm.frauddetection.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.uoc.tfm.frauddetection.model.Address;
import edu.uoc.tfm.frauddetection.model.Customer;
import edu.uoc.tfm.frauddetection.model.Merchant;
import edu.uoc.tfm.frauddetection.model.Transaction;

/**
 * A Jackson module so that we don't have to add Jackson annotations directly to our domain.
 *
 * @author Michael J. Simons
 */
final class TransactionModule extends SimpleModule {

	TransactionModule() {
		setMixInAnnotation(Customer.class, CustomerMixIn.class);
		setMixInAnnotation(Merchant.class, MerchantMixIn.class);
		setMixInAnnotation(Address.class, AddressMixIn.class);
		setMixInAnnotation(Transaction.class, TransactionMixIn.class);
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	abstract static class CustomerMixIn {
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	abstract static class AddressMixIn {
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	abstract static class TransactionMixIn {
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	abstract static class MerchantMixIn {
	}

}
