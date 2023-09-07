package Giuseppe.DigitalDelights.cartitem;

import java.util.UUID;

import Giuseppe.DigitalDelights.cart.Cart;
import Giuseppe.DigitalDelights.products.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue
	private UUID cartItemId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private int quantity;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	public CartItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

}
