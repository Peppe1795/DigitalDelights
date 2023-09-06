package Giuseppe.DigitalDelights.cart;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
public class Cart {
	@Id
	@GeneratedValue
	private UUID cartId;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany
	@JoinTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> products;

	private int quantity;
	@CreationTimestamp
	private Date createdDate;

	public Cart(User user, List<Product> products, int quantity) {
		this.user = user;
		this.products = products;
		this.quantity = quantity;
	}

}