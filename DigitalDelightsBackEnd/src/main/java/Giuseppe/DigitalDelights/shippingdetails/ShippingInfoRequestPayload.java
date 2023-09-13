package Giuseppe.DigitalDelights.shippingdetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShippingInfoRequestPayload {
	private String recipientName;
	private String shippingAddress;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String phoneNumber;

	public ShippingInfo toShippingInfo() {
		return new ShippingInfo(recipientName, shippingAddress, city, state, postalCode, country, phoneNumber);
	}
}
