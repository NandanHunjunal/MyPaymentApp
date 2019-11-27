package myapp.payment.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Payout;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.PayoutItem;
import com.paypal.api.payments.PayoutSenderBatchHeader;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;


/**
 * Provides some business functionalities to carryout Payouts. 
 * Spring context will autodetect these classes when annotation-based configuration and classpath scanning is used
 * 
 * 
 * @author Nandan
 *
 */
@Service
public class PayOutService {

	@Autowired
	public APIContext apiContext;

	/**
	 * Creates a {@link PayoutBatch} which is having the information of a receiver and the amount to be transfered.
	 * 
	 * @param amount
	 * @param emailId
	 * @return
	 * @throws PayPalRESTException
	 */
	public PayoutBatch createPayout(String amount, String emailId) throws PayPalRESTException {

		Payout payout = new Payout();
		PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();

		Random random = new Random();
		senderBatchHeader.setSenderBatchId(new Double(random.nextDouble()).toString())
				.setEmailSubject("You have a Payout!");

		Currency amountCurrency = new Currency();
		amountCurrency.setValue(amount).setCurrency("EUR");

		PayoutItem senderItem = new PayoutItem();
		senderItem.setRecipientType("Email").setNote("Some Note")
				.setReceiver(emailId).setSenderItemId("201404324234").setAmount(amountCurrency);

		List<PayoutItem> items = new ArrayList<PayoutItem>();
		items.add(senderItem);

		payout.setSenderBatchHeader(senderBatchHeader).setItems(items);

		return payout.create(apiContext, new HashMap<String, String>());
	}
}
