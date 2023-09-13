package Giuseppe.DigitalDelights.order;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import Giuseppe.DigitalDelights.orderitem.OrderItem;
import Giuseppe.DigitalDelights.shippingdetails.ShippingInfo;
import Giuseppe.DigitalDelights.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;

	@Enumerated(EnumType.STRING)
	private Status status;

	@CreationTimestamp
	private Date orderDate;

	private double totalPrice;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shipping_info_id")
	private ShippingInfo shippingInfo;

	public Order(User user, Status status, double totalPrice, ShippingInfo shippingInfo, List<OrderItem> orderItems) {
		this.user = user;
		this.status = status;
		this.totalPrice = totalPrice;
		this.shippingInfo = shippingInfo;
		this.orderItems = orderItems;
	}
}