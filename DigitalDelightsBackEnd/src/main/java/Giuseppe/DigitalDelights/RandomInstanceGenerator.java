package Giuseppe.DigitalDelights;

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
import Giuseppe.DigitalDelights.exception.BadRequestException;
import Giuseppe.DigitalDelights.prodotti.Category;
import Giuseppe.DigitalDelights.prodotti.Product;
import Giuseppe.DigitalDelights.prodotti.ProductRequestPayload;
import Giuseppe.DigitalDelights.prodotti.ProductService;
import Giuseppe.DigitalDelights.user.Role;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserRepository;
import Giuseppe.DigitalDelights.user.UserRequestPayload;
import Giuseppe.DigitalDelights.user.UserService;

@Component
public class RandomInstanceGenerator {
	private final UserService us;
	private final ProductService ps;
	private final AddressService ad;
	private final UserRepository ur;
	private final AddressRepository ar;
	Faker faker = new Faker(new Locale("it"));
	Random rnd = new Random();
	@Autowired
	PasswordEncoder bcrypt;

	@Autowired
	public RandomInstanceGenerator(UserService us, ProductService ps, AddressService ad, UserRepository ur,
			AddressRepository ar) {
		this.us = us;
		this.ps = ps;
		this.ad = ad;
		this.ur = ur;
		this.ar = ar;
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

				AddressRequestPayload addressPayload = new AddressRequestPayload(via, numeroCivico, localita, cap,
						comune, null);
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

}
