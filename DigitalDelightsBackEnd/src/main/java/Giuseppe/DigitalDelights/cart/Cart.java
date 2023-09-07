package Giuseppe.DigitalDelights.cart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import Giuseppe.DigitalDelights.cartitem.CartItem;
import Giuseppe.DigitalDelights.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private List<CartItem> cartItems = new ArrayList<>();

	@CreationTimestamp
	private Date createdDate;

	public Cart(User user) {

		this.user = user;
	}

}