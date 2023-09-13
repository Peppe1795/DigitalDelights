package Giuseppe.DigitalDelights.order;

import java.util.List;
import java.util.UUID;

import Giuseppe.DigitalDelights.orderitem.OrderItemRequestPayload;
import Giuseppe.DigitalDelights.shippingdetails.ShippingInfoRequestPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestPayload {
	private UUID userId;
	private double totalPrice;
	private ShippingInfoRequestPayload shippingInfoRequestPayload;
	private List<OrderItemRequestPayload> orderItems;
}
