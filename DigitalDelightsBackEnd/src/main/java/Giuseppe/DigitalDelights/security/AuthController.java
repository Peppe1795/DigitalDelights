package Giuseppe.DigitalDelights.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import Giuseppe.DigitalDelights.exception.UnauthorizedException;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserRequestPayload;
import Giuseppe.DigitalDelights.user.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	UserService userService;

	@Autowired
	JWTTools jwtTools;

	@Autowired
	PasswordEncoder bcrypt;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody UserRequestPayload body) {
		body.setPassword(bcrypt.encode(body.getPassword()));
		User created = userService.create(body);
		return created;
	}

	@PostMapping("/login")

	public ResponseEntity<TokenResponse> login(@RequestBody UserRequestPayload body) {

		User user = null;

		if (body.getEmail() != null) {
			user = userService.findByEmail(body.getEmail());
		} else {
			user = userService.findByUsername(body.getUsername());
		}

		if (user != null && bcrypt.matches(body.getPassword(), user.getPassword())) {
			String token = jwtTools.creaToken(user);
			return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);

		} else {
			throw new UnauthorizedException(
					"Credenziali non valide, verifica che la password o Email ed Username siano corrette");
		}
	}
}