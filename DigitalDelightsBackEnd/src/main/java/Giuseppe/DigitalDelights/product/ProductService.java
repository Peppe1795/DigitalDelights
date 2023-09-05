package Giuseppe.DigitalDelights.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	private final ProductRepository productRepo;

	@Autowired
	public ProductService(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}

	public Product create(ProductRequestPayload body) {
		Product newProduct = new Product(body.getSku(), body.getName(), body.getDescription(), body.getUnitPrice(),
				body.getImageUrl(), body.isActive(), body.getUnitsInStock());
		return productRepo.save(newProduct);
	}

}
