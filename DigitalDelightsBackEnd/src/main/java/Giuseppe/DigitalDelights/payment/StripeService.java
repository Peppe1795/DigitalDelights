package Giuseppe.DigitalDelights.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;

import jakarta.annotation.PostConstruct;

@Service
public class StripeService {
	@Value("${stripe.secret.key}")
	private String stripeSecretKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeSecretKey;
	}

	public Map<String, String> createCheckoutSession(double totalPrice) throws Exception {
		Stripe.apiKey = stripeSecretKey;

		Map<String, Object> params = new HashMap<>();
		List<Object> paymentMethodTypes = new ArrayList<>();
		paymentMethodTypes.add("card");
		params.put("payment_method_types", paymentMethodTypes);

		Map<String, Object> lineItem = new HashMap<>();
		Map<String, Object> priceData = new HashMap<>();
		priceData.put("currency", "eur");
		priceData.put("product_data", new HashMap<String, Object>() {
			{
				put("name", "Total Amount");
			}
		});
		priceData.put("unit_amount", (int) (totalPrice * 100)); //
		lineItem.put("price_data", priceData);
		lineItem.put("quantity", 1);

		List<Object> lineItems = new ArrayList<>();
		lineItems.add(lineItem);
		params.put("line_items", lineItems);

		params.put("mode", "payment");
		params.put("success_url", "http://localhost:4200");
		params.put("cancel_url", "http://localhost:4200");

		Session session = Session.create(params);

		Map<String, String> response = new HashMap<>();
		response.put("id", session.getId());
		return response;
	}
}