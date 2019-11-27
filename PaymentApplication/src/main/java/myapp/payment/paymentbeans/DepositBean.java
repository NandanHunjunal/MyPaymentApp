package myapp.payment.paymentbeans;

/**
 * A Bean class which creates a bean for giving attributes like amount.
 * 
 * Mainly this bean object is created whenever Deposit feature is executed
 * 
 * @author Nandan
 *
 */
public class DepositBean
{
	private double amount;

	public DepositBean() {
		super();
	}

	public DepositBean(double amount) {
		super();
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
