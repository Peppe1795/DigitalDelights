package Giuseppe.DigitalDelights.payment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("stripe")
@RestController
public class StripeController {

	@Autowired
	private StripeService stripeService;

	@GetMapping("/create-checkout-session")
	public Map<String, String> createSession() throws Exception {
		return stripeService.createCheckoutSession();
	}
}
