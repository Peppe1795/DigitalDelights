package Giuseppe.DigitalDelights.user;

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

import Giuseppe.DigitalDelights.address.Address;
import Giuseppe.DigitalDelights.address.AddressRepository;
import Giuseppe.DigitalDelights.exception.NotFoundException;
import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.products.ProductRepository;

@Service
public class UserService {
	private final UserRepository userRepo;
	private final ProductRepository pr;
	private final AddressRepository ar;

	@Autowired
	public UserService(UserRepository userRepo, ProductRepository pr, AddressRepository ar) {
		this.userRepo = userRepo;
		this.pr = pr;
		this.ar = ar;
	}

	public User create(UserRequestPayload body) {
		Address address = null;
		System.out.println(body);
		if (body.getAddress() != null) {
			System.out.println("sono qui");
			address = new Address(body.getAddress().getVia(), body.getAddress().getNumeroCivico(),
					body.getAddress().getLocalita(), body.getAddress().getCap(), body.getAddress().getComune(), null);
			// Salva l'indirizzo
			address = ar.save(address);
		}

		User newUser = new User(body.getName(), body.getUsername(), body.getLastName(), body.getEmail(),
				body.getPassword(), address, Role.USER);

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
		found.setLastName(body.getLastName());
		found.setEmail(body.getEmail());
		found.setPassword(body.getPassword());

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

//	// PRENDI L'ID DELL'UTENTE LOGGATO
//	public User getCurrentUser() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Object principal = authentication.getPrincipal();
//
//		String currentEmail;
//		if (principal instanceof User) {
//			currentEmail = ((User) principal).getEmail();
//		} else {
//			throw new NotFoundException("Utente non trovato nel contesto di autenticazione");
//		}
//
//		return userRepo.findByEmail(currentEmail)
//				.orElseThrow(() -> new NotFoundException("Utente con email " + currentEmail + " non trovato"));
//	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		if (principal instanceof User) {
			User user = (User) principal;
			String currentUserName = user.getName();
			Optional<User> userOptional = userRepo.findByName(currentUserName);

			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}

		// Se l'utente non è autenticato, restituisci null o un utente anonimo
		return null;
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
