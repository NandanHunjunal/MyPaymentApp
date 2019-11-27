package myapp.payment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Responsible to execute payments
 * 
 * @author Nandan
 *
 */
@Service
public class PaymentService {

	@Autowired
	APIContext apiContext;
	
	
	/**
	 * Executes the {@link Payment} for a given payment of a payer
	 * @param paymentId
	 * @param payerId
	 * @return
	 * {@link Payment} object contains all the payment information
	 * @throws PayPalRESTException
	 */
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException
	{
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}
}
