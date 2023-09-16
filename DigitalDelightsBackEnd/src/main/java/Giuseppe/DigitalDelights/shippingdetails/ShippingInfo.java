package Giuseppe.DigitalDelights.shippingdetails;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Giuseppe.DigitalDelights.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ShippingInfo {
	@Id
	@GeneratedValue
	private UUID shippingInfoId;
	private String recipientName;
	private String shippingAddress;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String phoneNumber;
	@JsonIgnore
	@OneToOne(mappedBy = "shippingInfo")
	private Order order;

	public ShippingInfo(String recipientName, String shippingAddress, String city, String state, String postalCode,
			String country, String phoneNumber) {
		this.recipientName = recipientName;
		this.shippingAddress = shippingAddress;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.phoneNumber = phoneNumber;
	}
}