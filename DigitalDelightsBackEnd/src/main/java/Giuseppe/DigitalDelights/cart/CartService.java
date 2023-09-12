package Giuseppe.DigitalDelights.cart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.cartitem.CartItem;
import Giuseppe.DigitalDelights.cartitem.CartItemRepository;
import Giuseppe.DigitalDelights.exception.NotFoundException;
import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.products.ProductRepository;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserRepository;

@Service
public class CartService {
	private final CartRepository cartRepo;
	private final CartItemRepository cartItemRepo;
	private final ProductRepository productRepo;
	private final UserRepository userRepo;

	@Autowired
	public CartService(CartRepository cartRepo, CartItemRepository cartItemRepo, ProductRepository productRepo,
			UserRepository userRepo) {
		this.cartRepo = cartRepo;
		this.cartItemRepo = cartItemRepo;
		this.productRepo = productRepo;
		this.userRepo = userRepo;
	}

	public Cart create(CartRequestPayload body) {
		Cart newCart = new Cart(body.getUser());
		return cartRepo.save(newCart);
	}

	public Page<Cart> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return cartRepo.findAll(pageable);
	}

	public Cart findById(UUID id) throws NotFoundException {
		return cartRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Cart findByIdAndUpdate(UUID id, CartRequestPayload body) throws NotFoundException {
		Cart found = this.findById(id);
		found.setUser(body.getUser());

		return cartRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Cart found = this.findById(id);
		cartRepo.delete(found);
	}

	public List<CartItem> getProductsInCart(UUID cartId) {
		Cart cart = findById(cartId);
		return cart.getCartItems();
	}

	public String getCurrentUserCartId() {
		// Ottieni l'oggetto di autenticazione dal contesto di sicurezza
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName(); // Supponendo che tu stia usando il nome utente come principal

			// Utilizza il nome utente per cercare l'utente nel tuo database
			Optional<User> userOptional = userRepo.findByUsername(username);

			if (userOptional.isPresent()) {
				User currentUser = userOptional.get();

				// Controlla se l'utente ha un carrello associato
				if (currentUser.getCart() != null) {
					return currentUser.getCart().getCartId().toString();
				}
			}
		}

		// Se l'utente non è autenticato o non ha un carrello associato, restituisci
		// null
		return null;
	}

	public Cart addProductToCart(UUID cartId, UUID productId, int quantity) {
		Cart cart = findById(cartId);
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new NotFoundException("Prodotto non trovato con ID: " + productId));

		CartItem existingCartItem = cart.getCartItems().stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)).findFirst().orElse(null);

		if (existingCartItem != null) {
			existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
			cartItemRepo.save(existingCartItem);
		} else {
			CartItem cartItem = new CartItem(product, cart, quantity);
			cart.getCartItems().add(cartItem);
			cartItemRepo.save(cartItem);
		}

		return cart;
	}

	public Cart updateProductInCart(UUID cartId, UUID productId, int quantity) {
		Cart cart = findById(cartId);
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new NotFoundException("Prodotto non trovato con ID: " + productId));

		CartItem cartItem = cart.getCartItems().stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)).findFirst()
				.orElseThrow(() -> new NotFoundException("Prodotto non presente nel carrello"));

		cartItem.setQuantity(quantity);
		cartItemRepo.save(cartItem);

		return cart;
	}

	public Cart removeProductFromCart(UUID cartId, UUID productId) {
		Cart cart = findById(cartId);
		CartItem cartItem = cart.getCartItems().stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)).findFirst()
				.orElseThrow(() -> new NotFoundException("Prodotto non presente nel carrello"));

		cart.removeCartItem(cartItem);
		cartItemRepo.delete(cartItem);

		return cart;
	}

}
