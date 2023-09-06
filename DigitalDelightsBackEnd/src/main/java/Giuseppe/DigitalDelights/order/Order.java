package Giuseppe.DigitalDelights.order;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import Giuseppe.DigitalDelights.prodotti.Product;
import Giuseppe.DigitalDelights.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue
	private UUID orderId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany
	@JoinTable(name = "order_items", joinColumns = @JoinColumn(name = "orders_id"), inverseJoinColumns = @JoinColumn(name = "products_id"))
	private List<Product> products;
	@CreationTimestamp
	private Date orderDate;

	private double totalPrice;

	public Order(User user, List<Product> products, Date orderDate, double totalPrice) {
		this.user = user;
		this.products = products;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
	}

}
