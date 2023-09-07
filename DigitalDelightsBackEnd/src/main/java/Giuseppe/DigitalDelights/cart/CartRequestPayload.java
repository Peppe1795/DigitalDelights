package Giuseppe.DigitalDelights.cart;

import Giuseppe.DigitalDelights.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartRequestPayload {

	private User user;
}
