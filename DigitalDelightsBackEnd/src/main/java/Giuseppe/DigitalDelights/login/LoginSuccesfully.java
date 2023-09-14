package Giuseppe.DigitalDelights.login;

import Giuseppe.DigitalDelights.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginSuccesfully {
	String accessToken;
	private Role role;
}
