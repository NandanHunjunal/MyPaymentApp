package myapp.payment.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.FuturePayment;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Provides some business functionalities to carryout Deposit in Paypal. Spring
 * context will autodetect these classes when annotation-based configuration and
 * classpath scanning is used
 * 
 * 
 * @author Nandan
 *
 */

@Service
public class DepositService {

	@Autowired
	public APIContext apiContext;

	public Payment createPayment(double amountValue, String cancelUrl, String successUrl) throws PayPalRESTException {
		Amount amount = new Amount();
		amount.setCurrency("EUR");
		amount.setTotal(
				String.format("%.2f", new BigDecimal(amountValue).setScale(2, RoundingMode.HALF_UP).doubleValue()));

		Transaction transaction = new Transaction();
		transaction.setDescription("Test");
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		FuturePayment futurePayment = new FuturePayment();
		futurePayment.setIntent("authorize");
		futurePayment.setPayer(payer);
		futurePayment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		futurePayment.setRedirectUrls(redirectUrls);

		Payment createdPayment = futurePayment.create(apiContext); // create(apiContext, correlationId);

		return createdPayment;
	}
}
