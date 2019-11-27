package myapp.payment.paymentbeans;

/**
 * A Bean class which creates a bean for giving attributes like amount and email
 * 
 * Mainly this bean object is created whenever Payout feature is executed
 * 
 * @author Nandan
 *
 */
public class PayoutBean
{
	private String amount;
	private String email;

	public PayoutBean(String amount, String email) {
		super();
		this.amount = amount;
		this.email = email;
	}

	public PayoutBean() {
		super();
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
