package Giuseppe.DigitalDelights.orderitem;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Giuseppe.DigitalDelights.order.Order;
import Giuseppe.DigitalDelights.products.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue
	private UUID orderItemId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private Order order;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private int quantity;
	private double priceAtPurchase;

	public OrderItem(Order order, Product product, int quantity) {
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.priceAtPurchase = product.getPrice();
	}
}