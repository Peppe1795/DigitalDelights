package Giuseppe.DigitalDelights.order;

import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.shippingdetails.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DetailedOrderResponse {

    private UUID orderId;
    private Status status;
    private double totalPrice;
    private ShippingInfo shippingInfo;
    private List<Product> products;  
}
