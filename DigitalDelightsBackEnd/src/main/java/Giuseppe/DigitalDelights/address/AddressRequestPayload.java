package Giuseppe.DigitalDelights.address;

import Giuseppe.DigitalDelights.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressRequestPayload {
	private String via;
	private int numeroCivico;
	private String località;
	private String cap;
	private String comune;
	private User user;
}
