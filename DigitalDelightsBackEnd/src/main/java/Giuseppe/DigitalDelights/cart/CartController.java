package Giuseppe.DigitalDelights.cart;

import java.util.List;
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

import Giuseppe.DigitalDelights.cartitem.CartItem;

@RestController
@RequestMapping("/cart")
public class CartController {
	private final CartService cartSrv;

	@Autowired
	public CartController(CartService cartSrv) {
		this.cartSrv = cartSrv;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<Cart> getCart(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "cartId") String sortBy) {
		return cartSrv.find(page, size, sortBy);
	}

	@GetMapping("/{cartId}")
	public Cart findById(@PathVariable UUID cartId) {
		return cartSrv.findById(cartId);

	}

	@GetMapping("/{cartId}/products")
	public List<CartItem> getProductsInCart(@PathVariable UUID cartId) {
		return cartSrv.getProductsInCart(cartId);
	}

	@PostMapping("/{cartId}/product/{productId}")
	public ResponseEntity<String> addProductToCart(@PathVariable UUID cartId, @PathVariable UUID productId,
			@RequestParam int quantity) {
		cartSrv.addProductToCart(cartId, productId, quantity);
		return ResponseEntity.ok("Prodotto aggiunto con successo al carrello.");
	}

	@PutMapping("/{cartId}/product/{productId}")
	public ResponseEntity<String> updateProductInCart(@PathVariable UUID cartId, @PathVariable UUID productId,
			@RequestParam int quantity) {
		cartSrv.updateProductInCart(cartId, productId, quantity);
		return ResponseEntity.ok("Quantità di prodotto aggiornata con successo.");
	}

	@GetMapping("/current-user-cart-id")
	public ResponseEntity<?> getCurrentUserCartId() {
		try {
			String cartId = cartSrv.getCurrentUserCartId();

			if (cartId != null && !cartId.isEmpty()) {
				return ResponseEntity.ok(cartId);
			} else {
				// Aggiungi un log qui per capire se entra in questa condizione
				System.out.println("Cart ID non trovato per l'utente corrente.");
				return ResponseEntity.notFound().build();
			}
		} catch (Exception ex) {
			System.out.println("Errore nel recupero dell'ID del carrello: " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore nel recupero dell'ID del carrello dell'utente.");
		}
	}

	@DeleteMapping("/{cartId}/product/{productId}")
	public ResponseEntity<String> removeProductFromCart(@PathVariable UUID cartId, @PathVariable UUID productId) {
		cartSrv.removeProductFromCart(cartId, productId);
		return ResponseEntity.ok("Prodotto rimosso con successo dal carrello.");
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public Cart saveCart(@RequestBody CartRequestPayload body) {
		Cart created = cartSrv.create(body);

		return created;
	}

	@PutMapping("/{cartId}")
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
