package Giuseppe.DigitalDelights.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {
	private final UserService userSrv;

	@Autowired
	public UserController(UserService userSrv) {
		this.userSrv = userSrv;
	}

	@GetMapping
	public Page<User> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "userId") String sortBy) {
		return userSrv.find(page, size, sortBy);
	}

	@GetMapping("/{userId}")
	public User findUserById(@PathVariable UUID userId) {
		return userSrv.findById(userId);

	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody UserRequestPayload body) {
		User created = userSrv.create(body);
		return created;
	}

	@PutMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public User updateUser(@PathVariable UUID userId, @RequestBody UserRequestPayload body) {
		return userSrv.findByIdAndUpdate(userId, body);
	}

	@DeleteMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
		userSrv.findByIdAndDelete(userId);
		return ResponseEntity.ok("User eliminato con successo.");
	}
}
