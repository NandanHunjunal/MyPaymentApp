package myapp.payment.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.paypal.base.rest.APIContext;

import myapp.payment.configurations.MyPaymentPaypalConfiguration;
import myapp.payment.services.PayOutService;
import myapp.payment.services.PaymentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= MyPaymentPaypalConfiguration.class)
@WebMvcTest(value = MyPaymentController.class)
class MyPaymentControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	APIContext apiContext;
	
	@InjectMocks
    MyPaymentController myPaymentController;
	
	@MockBean
	PayOutService payoutService;
	
	@MockBean
	PaymentService paymentService;
	
	
	@Test
	public void testAccessToken() throws Exception
	{
		assertEquals("Bearer A21AAE6o4nqv9G0LnM13uOquEZWUg7F4UYHjudxEjVbHLR3kOCObGieO5_RDtG-A-rhJg42Dh5biAt7wN1YQtP1x2Xbr89HcQ", apiContext.getAccessToken());
	}

	@Test
	void test() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		assertEquals("home", result.getResponse().getContentAsString());
	}
	
}
