package Giuseppe.DigitalDelights.order;

import java.util.Date;
import java.util.List;

import Giuseppe.DigitalDelights.prodotti.Product;
import Giuseppe.DigitalDelights.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestPayload {

	private User user;

	private List<Product> products;

	private Date orderDate;

	private double totalPrice;
}
