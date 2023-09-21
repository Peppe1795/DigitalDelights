package Giuseppe.DigitalDelights.cart;

import java.util.ArrayList;
import java.util.Collections;

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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName(); 

			
			Optional<User> userOptional = userRepo.findByUsername(username);

			if (userOptional.isPresent()) {
				User currentUser = userOptional.get();

				
				if (currentUser.getCart() != null) {
					return currentUser.getCart().getCartId().toString();
				}
			}
		}

		
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
	
	public Cart clearCart(UUID cartId) {
	    Cart cart = findById(cartId);
	    List<CartItem> cartItems = cart.getCartItems();

	    if(cartItems != null && !cartItems.isEmpty()) {
	        for (CartItem item : new ArrayList<>(cartItems)) {
	            cart.removeCartItem(item);  
	            cartItemRepo.delete(item);  
	        }
	        cart.setCartItems(Collections.emptyList());  
	        return cartRepo.save(cart); 
	    }
	    return cart;
	}







	 
}
