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

import Giuseppe.DigitalDelights.products.Product;

@RequestMapping("/user")
@RestController
public class UserController {
	private final UserService userSrv;

	@Autowired
	public UserController(UserService userSrv) {
		this.userSrv = userSrv;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<User> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "userId") String sortBy) {
		return userSrv.find(page, size, sortBy);
	}

	@GetMapping("/{userId}")
	public User findUserById(@PathVariable UUID userId) {
		return userSrv.findById(userId);

	}

	@GetMapping("/current")
	public ResponseEntity<User> getCurrentUser() {
		User user = userSrv.getCurrentUser();

		if (user != null) {
			return ResponseEntity.ok(user);
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody UserRequestPayload body) {
		User created = userSrv.create(body);
		return created;
	}

	@PutMapping("/{userId}")
	public User updateUser(@PathVariable UUID userId, @RequestBody UserRequestPayload body) {
		return userSrv.findByIdAndUpdate(userId, body);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
		userSrv.findByIdAndDelete(userId);
		return ResponseEntity.ok("User eliminato con successo.");
	}

	@PostMapping("/addWishList/{productId}")
	public ResponseEntity<?> addFavorite(@PathVariable UUID productId) {
		userSrv.addFavoriteProduct(productId);
		return ResponseEntity.ok("Prodotto aggiunto ai preferiti");
	}

	@DeleteMapping("/removeWishList/{productId}")
	public ResponseEntity<?> removeFavorite(@PathVariable UUID productId) {
		userSrv.removeFavoriteProduct(productId);
		return ResponseEntity.ok("Prodotto rimosso dai preferiti");
	}

	@GetMapping("/wishList")
	public ResponseEntity<Page<Product>> getUserPreferiti(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<Product> favorites = userSrv.getUserProductPreferite(page, size);
		return ResponseEntity.ok(favorites);
	}
}
