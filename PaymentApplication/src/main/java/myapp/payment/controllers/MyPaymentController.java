package myapp.payment.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.base.rest.PayPalRESTException;

import myapp.payment.paymentbeans.DepositBean;
import myapp.payment.paymentbeans.PayoutBean;
import myapp.payment.services.DepositService;
import myapp.payment.services.LoginService;
import myapp.payment.services.PayOutService;
import myapp.payment.services.PaymentService;
import myapp.payment.utilities.JSonUtility;
import myapp.payment.utilities.ResponseUrlCreator;

/**
 * Controls the data flow into model object and updates the view whenever data changes.
 * 
 * @author Nandan
 */

@Controller
public class MyPaymentController {
	
	public static final String PAYPAL_SUCCESS_URL = "pay/success";
	public static final String PAYPAL_CANCEL_URL = "pay/cancel";

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	LoginService loginService;

	@Autowired
	PaymentService paymentService;
	
	@Autowired
	PayOutService payOutService;

	@Autowired
	DepositService depositService;

	JSonUtility jSonUtility = new JSonUtility();
	
	ResponseUrlCreator responseUrlCreator = new ResponseUrlCreator();
	
	@RequestMapping("/")
	public String home() {
		return "home";
	}

	@RequestMapping("/deposit")
	public String deposit() {
		return "deposit";
	}

	@RequestMapping("/payout")
	public String payout() {
		return "payout";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "login")
	public String login(HttpServletRequest request) {
		String cancelUrl = responseUrlCreator.createURL(request) + "/" + PAYPAL_CANCEL_URL;
		String successUrl = responseUrlCreator.createURL(request) + "/" + PAYPAL_SUCCESS_URL;
		try {
			Payment payment = loginService.createPayment(cancelUrl,
					successUrl);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return "redirect:" + links.getHref();
				}
			}
			
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "deposit")
	public String pay(HttpServletRequest request, @ModelAttribute("depositBean") DepositBean depositBean)
			throws PayPalRESTException {

		String cancelUrl = responseUrlCreator.createURL(request) + "/" + PAYPAL_CANCEL_URL;
		String successUrl = responseUrlCreator.createURL(request) + "/" + PAYPAL_SUCCESS_URL;
		try {
			Payment payment = depositService.createPayment(depositBean.getAmount(), cancelUrl,
					successUrl);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return "redirect:" + links.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";	
		
	}

	

//	@RequestMapping(method = RequestMethod.GET, value = "login")
//	public String login(HttpServletRequest request) throws PayPalRESTException {
//		Map<String, String> configurationMap = new HashMap<String, String>();
//	    configurationMap.put("mode", "sandbox");
//
//	    APIContext apiContext = new APIContext();
//	    apiContext.setConfigurationMap(configurationMap);
//		
//	    List<String> scopelist = new ArrayList<String>();
//	    scopelist.add("openid");
//	    scopelist.add("email");
//	    String redirectURI = "success";
//
//	    ClientCredentials clientCredentials = new ClientCredentials();
//	    clientCredentials.setClientID("AZoFxb19uUz7TsqJOeo3nJQ-6-2_NRnd8-DnX2F8wei7ILbZs0fnNA88Ok7Og7_PayBm63zTk1Lt1yfK");
//
//	    String redirectUrl = Session.getRedirectURL(redirectURI, scopelist, apiContext, clientCredentials);
//	
//		return redirectUrl;
//	}

	
	@RequestMapping(method = RequestMethod.POST, value = "payout")
	public String pay(HttpServletRequest request, @ModelAttribute("payoutBean") PayoutBean payoutBean)
			throws PayPalRESTException {

		PayoutBatch payout = payOutService.createPayout(payoutBean.getAmount(),
				payoutBean.getEmail());

		for (Links links : payout.getLinks()) {
			links.getHref();

			if (jSonUtility.evaluateJSonResponse(links, payOutService.apiContext.getAccessToken())) {
				return "success";
			} else {
				return "cancel";
			}
		}

		return "redirect:/";
	}

	
	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
	public String cancelPay() {
		return "cancel";
	}

	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
		try {
			
			Payment payment = paymentService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				return "success";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}

}
