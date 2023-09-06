package Giuseppe.DigitalDelights.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;
import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.products.ProductRepository;

@Service
public class UserService {
	private final UserRepository userRepo;
	private final ProductRepository pr;

	@Autowired
	public UserService(UserRepository userRepo, ProductRepository pr) {
		this.userRepo = userRepo;
		this.pr = pr;
	}

	public User create(UserRequestPayload body) {
		User newUser = new User(body.getName(), body.getUsername(), body.getLastName(), body.getEmail(),
				body.getPassword(), body.getAddress(), body.getRole());
		return userRepo.save(newUser);
	}

	public Page<User> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return userRepo.findAll(pageable);
	}

	public User findById(UUID id) throws NotFoundException {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public User findByIdAndUpdate(UUID id, UserRequestPayload body) throws NotFoundException {
		User found = this.findById(id);
		found.setName(body.getName());
		found.setUsername(body.getUsername());
		;
		found.setLastName(body.getLastName());
		found.setEmail(body.getEmail());
		found.setPassword(body.getPassword());
		found.setRole(body.getRole());

		return userRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		User found = this.findById(id);
		userRepo.delete(found);
	}

	public User findByEmail(String email) {
		return userRepo.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato"));
	}

	public User findByUsername(String username) {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("Username" + username + "non corrispondente"));
	}

	// PRENDI L'ID DELL'UTENTE LOGGATO
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		String currentEmail;
		if (principal instanceof User) {
			currentEmail = ((User) principal).getEmail();
		} else {
			throw new NotFoundException("Utente non trovato nel contesto di autenticazione");
		}

		return userRepo.findByEmail(currentEmail)
				.orElseThrow(() -> new NotFoundException("Utente con email " + currentEmail + " non trovato"));
	}

	public void addFavoriteProduct(UUID productId) {
		User user = this.getCurrentUser();
		Product product = pr.findById(productId)
				.orElseThrow(() -> new NotFoundException("L'id non corrissponde a nessun prodotto " + productId));

		user.getFavoriteProducts().add(product);
		userRepo.save(user);
	}

	public void removeFavoriteProduct(UUID productId) {
		User user = this.getCurrentUser();
		Product product = pr.findById(productId)
				.orElseThrow(() -> new NotFoundException("L'id non corrissponde a nessun prodotto" + productId));

		user.getFavoriteProducts().remove(product);
		userRepo.save(user);
	}

	public Page<Product> getUserProductPreferite(int page, int size) {
		User currentUser = getCurrentUser();
		Pageable pageable = PageRequest.of(page, size);
		Page<Product> favorites = userRepo.findFavoriteProductsByUserId(currentUser.getUserId(), pageable);

		if (favorites.isEmpty()) {
			throw new NotFoundException("La tua lista dei preferiti è vuota");
		}

		return favorites;
	}
}
