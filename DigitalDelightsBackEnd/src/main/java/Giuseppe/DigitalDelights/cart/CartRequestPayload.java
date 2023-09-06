package Giuseppe.DigitalDelights.cart;

import java.util.List;

import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartRequestPayload {

	private User user;
	private List<Product> products;

	private int quantity;
}
