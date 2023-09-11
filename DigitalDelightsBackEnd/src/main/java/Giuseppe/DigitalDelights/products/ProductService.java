package Giuseppe.DigitalDelights.products;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserRepository;

@Service
public class ProductService {
	private final ProductRepository productRepo;

	private final UserRepository userRepo;

	@Autowired
	public ProductService(ProductRepository productRepo, UserRepository userRepo) {
		this.productRepo = productRepo;
		this.userRepo = userRepo;
	}

	public Product create(ProductRequestPayload body) {
		Product newProduct = new Product(body.getName(), body.getDescription(), body.getPrice(), body.getImageUrl(),
				body.isActive(), body.getUnitsInStock(), body.getCategory());
		return productRepo.save(newProduct);
	}

	public Page<Product> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return productRepo.findAll(pageable);
	}

	public Product findById(UUID id) throws NotFoundException {
		return productRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Product findByIdAndUpdate(UUID id, ProductRequestPayload body) throws NotFoundException {
		Product found = this.findById(id);
		found.setName(body.getName());
		found.setDescription(body.getDescription());
		found.setPrice(body.getPrice());
		found.setImageUrl(body.getImageUrl());
		found.setActive(body.isActive());
		found.setUnitsInStock(body.getUnitsInStock());
		found.setCategory(body.getCategory());

		return productRepo.save(found);
	}

	public Page<Product> findByPartOfName(String partOfName, int page, int size, String sortBy) {
		Pageable productPageable = PageRequest.of(page, size, Sort.by(sortBy));
		return productRepo.findByNameContainingIgnoreCase(partOfName, productPageable);
	}

	public void findByIdAndDelete(UUID productId) throws NotFoundException {
		Product productToDelete = productRepo.findById(productId)
				.orElseThrow(() -> new NotFoundException("Prodotto non trovato con ID: " + productId));

		List<User> usersWithFavoriteProduct = userRepo.findAllByFavoriteProductsContaining(productToDelete);

		for (User user : usersWithFavoriteProduct) {
			user.getFavoriteProducts().remove(productToDelete);
			userRepo.save(user);
		}

		productRepo.delete(productToDelete);
	}

	public Page<Product> findFilteredProducts(String name, Category category, Double minPrice, Double maxPrice,
			String sortBy, Pageable pageable) {
		return productRepo.findFilteredProducts(name, category, minPrice, maxPrice, sortBy, pageable);
	}

}
