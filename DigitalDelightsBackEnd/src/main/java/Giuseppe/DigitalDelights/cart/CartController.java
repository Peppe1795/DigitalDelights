package Giuseppe.DigitalDelights.cart;

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

@RestController
@RequestMapping("/user/cart")
public class CartController {
	private final CartService cartSrv;

	@Autowired
	public CartController(CartService cartSrv) {
		this.cartSrv = cartSrv;
	}

	@GetMapping
	public Page<Cart> getCart(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "cartId") String sortBy) {
		return cartSrv.find(page, size, sortBy);
	}

	@GetMapping("/{cartId}")
	public Cart findById(@PathVariable UUID cartId) {
		return cartSrv.findById(cartId);

	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public Cart saveCart(@RequestBody CartRequestPayload body) {
		Cart created = cartSrv.create(body);

		return created;
	}

	@PutMapping("/{cartId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public Cart updateCart(@PathVariable UUID cartId, @RequestBody CartRequestPayload body) {
		return cartSrv.findByIdAndUpdate(cartId, body);
	}

	@DeleteMapping("/{cartId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteCart(@PathVariable UUID cartId) {
		cartSrv.findByIdAndDelete(cartId);
		return ResponseEntity.ok("Carrello eliminato con successo.");

	}
}
