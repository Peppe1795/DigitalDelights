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
	private String numeroCivico;
	private String località;
	private int cap;
	private User user;
}
