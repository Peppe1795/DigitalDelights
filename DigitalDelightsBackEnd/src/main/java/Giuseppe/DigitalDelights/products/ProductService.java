package Giuseppe.DigitalDelights.products;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;

@Service
public class ProductService {
	private final ProductRepository productRepo;

	@Autowired
	public ProductService(ProductRepository productRepo) {
		this.productRepo = productRepo;
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

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Product found = this.findById(id);
		productRepo.delete(found);
	}

	public Page<Product> findByPartOfName(String parteDelNome, int page, int size, String sortBy) {
		Pageable productPageable = PageRequest.of(page, size, Sort.by(sortBy));
		return productRepo.findByNameContainingIgnoreCase(parteDelNome, productPageable);
	}
}
