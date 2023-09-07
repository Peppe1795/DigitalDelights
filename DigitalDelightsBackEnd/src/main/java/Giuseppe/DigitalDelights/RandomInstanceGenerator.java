package Giuseppe.DigitalDelights;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import Giuseppe.DigitalDelights.address.Address;
import Giuseppe.DigitalDelights.address.AddressRepository;
import Giuseppe.DigitalDelights.address.AddressRequestPayload;
import Giuseppe.DigitalDelights.address.AddressService;
import Giuseppe.DigitalDelights.cart.Cart;
import Giuseppe.DigitalDelights.cart.CartRequestPayload;
import Giuseppe.DigitalDelights.cart.CartService;
import Giuseppe.DigitalDelights.exception.BadRequestException;
import Giuseppe.DigitalDelights.order.OrderRequestPayload;
import Giuseppe.DigitalDelights.order.OrderService;
import Giuseppe.DigitalDelights.products.Category;
import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.products.ProductRepository;
import Giuseppe.DigitalDelights.products.ProductRequestPayload;
import Giuseppe.DigitalDelights.products.ProductService;
import Giuseppe.DigitalDelights.reviews.Reviews;
import Giuseppe.DigitalDelights.reviews.ReviewsRepository;
import Giuseppe.DigitalDelights.user.Role;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserRepository;
import Giuseppe.DigitalDelights.user.UserRequestPayload;
import Giuseppe.DigitalDelights.user.UserService;
import jakarta.transaction.Transactional;

@Component
public class RandomInstanceGenerator {
	private final UserService us;
	private final ProductService ps;
	private final AddressService ad;
	private final OrderService os;
	private final UserRepository ur;
	private final AddressRepository ar;
	private final ProductRepository pr;
	private final CartService cs;
	private final ReviewsRepository rr;

	Faker faker = new Faker(new Locale("it"));
	Random rnd = new Random();
	@Autowired
	PasswordEncoder bcrypt;

	@Autowired
	public RandomInstanceGenerator(UserService us, ProductService ps, AddressService ad, UserRepository ur,
			AddressRepository ar, ProductRepository pr, OrderService os, CartService cs, ReviewsRepository rr) {
		this.us = us;
		this.ps = ps;
		this.ad = ad;
		this.ur = ur;
		this.ar = ar;
		this.pr = pr;
		this.os = os;
		this.cs = cs;
		this.rr = rr;
	}

	public void generateRandomAddressAndUser(int numeroIstanze) {
		for (int i = 0; i < numeroIstanze; i++) {
			try {
				// Step 1: Genera e salva User temporaneo (senza Address)
				String name = faker.name().firstName();
				String lastName = faker.name().lastName();
				String username = faker.name().username();
				String email = faker.internet().emailAddress();
				String password = bcrypt.encode(faker.lorem().characters(6, 12));
				Role role = Role.values()[rnd.nextInt(Role.values().length)];

				UserRequestPayload tempUserPayload = new UserRequestPayload(name, username, lastName, email, password,
						null, role);
				User newUser = us.create(tempUserPayload);

				// Step 2: Genera e salva Address (senza User)
				String via = faker.address().streetName();
				int numeroCivico = rnd.nextInt(1000);
				String localita = faker.ancient().titan();
				String cap = String.valueOf(rnd.nextInt(99999 - 10000) + 10000);
				String comune = faker.address().state();

				AddressRequestPayload addressPayload = new AddressRequestPayload(via, numeroCivico, localita, comune,
						cap, null);
				Address newAddress = ad.create(addressPayload);

				// Step 3: Aggiorna il nuovo User con il nuovo Address
				newUser.setAddress(newAddress);

				// Step 4: Aggiorna il nuovo Address con il nuovo User
				newAddress.setUser(newUser);

				// Step 5: Salva entrambi gli oggetti aggiornati nel database
				ur.save(newUser);
				ar.save(newAddress);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Errore nella generazione degli utenti e indirizzi!");
			}
		}
	}

	public Product randomProductGenerator(int numeroIstanze) {
		Faker faker = new Faker();
		for (int i = 0; i < numeroIstanze; i++) {
			try {
				String name = faker.commerce().productName();
				String description = faker.lorem().sentence();
				double price = faker.number().randomDouble(2, 1, 3000);
				String imageUrl = faker.internet().image();
				boolean active = faker.bool().bool();
				int unitsInStock = faker.number().numberBetween(0, 1000);
				Category category = Category.values()[faker.number().numberBetween(0, Category.values().length - 1)];
				ProductRequestPayload productPayload = new ProductRequestPayload(name, description, price, imageUrl,
						active, unitsInStock, category);

				ps.create(productPayload);

			} catch (BadRequestException e) {
				e.printStackTrace();
				System.out.println("Errore nella generazione dei prodotti!");
			}
		}
		return null;
	}

	public int generateRandomNumberSeries(int cifre) {
		int n = cifre;
		int min = (int) Math.pow(10, n - 1);
		int max = (int) Math.pow(10, n) - 1;
		int rndSeries = min + (int) (Math.random() * (max - min + 1));
		return rndSeries;
	}

	public void generateRandomOrders(int numberOfOrders) {
		for (int i = 0; i < numberOfOrders; i++) {
			try {
				// Ottieni un utente casuale dall'elenco di tutti gli utenti
				List<User> allUsers = ur.findAll();
				User randomUser = allUsers.get(new Random().nextInt(allUsers.size()));

				// Ottieni una lista di prodotti casuali dall'elenco di tutti i prodotti
				List<Product> allProducts = pr.findAll();
				List<Product> randomProducts = new ArrayList<>();
				Random rand = new Random();
				int numberOfProducts = rand.nextInt(5) + 1; // Numero casuale di prodotti nell'ordine
				for (int j = 0; j < numberOfProducts; j++) {
					randomProducts.add(allProducts.get(rand.nextInt(allProducts.size())));
				}

				// Calcola il prezzo totale dell'ordine
				double totalPrice = 0.0;
				for (Product product : randomProducts) {
					totalPrice += product.getPrice();
				}

				// Crea e inizializza l'oggetto OrderRequestPayload
				OrderRequestPayload orderPayload = new OrderRequestPayload();
				orderPayload.setUser(randomUser);
				orderPayload.setProducts(randomProducts);
				orderPayload.setTotalPrice(totalPrice);
				// Puoi anche impostare una data casuale qui

				// Salva l'ordine (supponendo che tu abbia un metodo 'create' in un servizio di
				// Order)
				os.create(orderPayload);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Errore nella generazione degli ordini!");
			}
		}
	}

	@Transactional
	public Cart createTestCart() {
		// Creazione e salvataggio dell'utente
		User user = new User("username", "name", "lastname", "email@email.com", "password", null, Role.USER);
		ur.save(user); // Supponendo che userRepository è il tuo bean UserRepository

		// Creazione e salvataggio dei prodotti
		Product product1 = new Product("Product1", "Description1", 20.0, "imageURL", true, 5, Category.SMARTPHONE);
		Product product2 = new Product("Product2", "Description2", 30.0, "imageURL", true, 3, Category.SMARTWATCH);
		pr.save(product1); // Supponendo che productRepository è il tuo bean ProductRepository
		pr.save(product2);

		// Creazione del carrello
		List<Product> products = List.of(product1, product2);
		CartRequestPayload cartPayload = new CartRequestPayload(user);

		return cs.create(cartPayload); // Supponendo che cs è il tuo bean CartService
	}

	public Reviews createSampleReview() {
		User sampleUser = new User("userdsname", "namdde", "lastnddame", "emddail@email.com", "paddssword", null,
				Role.USER); // Popola questo oggetto con dati di esempio
		Product sampleProduct = new Product("Product2dd", "Descriptddion2", 32.0, "imageURL", true, 3,
				Category.SMARTWATCH); // Popola questo oggetto con dati di esempio

		Reviews sampleReview = new Reviews(5, "Questa è una recensione di test", sampleUser, sampleProduct);
		return rr.save(sampleReview);
	}
}
