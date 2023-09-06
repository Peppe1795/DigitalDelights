package Giuseppe.DigitalDelights.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginPayload {
	String email;
	String password;
}
