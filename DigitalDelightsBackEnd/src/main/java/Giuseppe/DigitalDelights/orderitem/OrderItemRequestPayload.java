package Giuseppe.DigitalDelights.orderitem;

import java.util.UUID;

import Giuseppe.DigitalDelights.products.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemRequestPayload {
	private UUID productId;
	private int quantity;

	public OrderItem toOrderItem(Product product) {
		return new OrderItem(null, product, quantity);
	}
}