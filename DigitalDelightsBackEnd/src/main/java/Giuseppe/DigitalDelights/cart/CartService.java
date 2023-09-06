package Giuseppe.DigitalDelights.cart;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;

@Service
public class CartService {
	private final CartRepository cartRepo;

	@Autowired
	public CartService(CartRepository cartRepo) {
		this.cartRepo = cartRepo;
	}

	public Cart create(CartRequestPayload body) {
		Cart newCart = new Cart(body.getUser(), body.getProducts(), body.getQuantity());
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
		found.setProducts(body.getProducts());
		found.setQuantity(body.getQuantity());

		return cartRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Cart found = this.findById(id);
		cartRepo.delete(found);
	}

//	@Transactional
//	public Cart createCart(UUID userId, Optional<List<UUID>> productIds) {
//		// Verifica se l'utente esiste
//		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//		// Verifica se un carrello esiste già per questo utente
//		Optional<Cart> existingCartOpt = cartRepository.findByUserId(userId);
//		if (existingCartOpt.isPresent()) {
//			throw new IllegalStateException("A cart already exists for this user");
//		}
//
//		// Crea un nuovo carrello e associa con l'utente
//		Cart newCart = new Cart();
//		newCart.setUser(user);
//		newCart.setTotalPrice(0.0);
//
//		// Se forniti, aggiungi i prodotti al carrello
//		if (productIds.isPresent()) {
//			List<Product> products = productService.findByIds(productIds.get());
//			newCart.setProducts(products);
//			double totalPrice = products.stream().mapToDouble(Product::getPrice).sum();
//			newCart.setTotalPrice(totalPrice);
//		}
//
//		// Salva il nuovo carrello
//		cartRepository.save(newCart);
//
//		return newCart;
//	}
}
