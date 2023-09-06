package Giuseppe.DigitalDelights.prodotti;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/product")
public class ProductController {
	private final ProductService productSrv;

	@Autowired
	public ProductController(ProductService productSrv) {
		this.productSrv = productSrv;
	}

	@GetMapping
	public Page<Product> getProduct(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "productId") String sortBy) {
		return productSrv.find(page, size, sortBy);
	}

	@GetMapping("/{productId}")
	public Product findById(@PathVariable UUID productId) {
		return productSrv.findById(productId);

	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public Product saveIndirizzo(@RequestBody ProductRequestPayload body) {
		Product created = productSrv.create(body);

		return created;
	}

	@PutMapping("/{productId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public Product updateProduct(@PathVariable UUID productId, @RequestBody ProductRequestPayload body) {
		return productSrv.findByIdAndUpdate(productId, body);
	}

	@DeleteMapping("/{productId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteProduct(@PathVariable UUID productId) {
		productSrv.findByIdAndDelete(productId);
		return ResponseEntity.ok("Prodotto eliminato con successo.");

	}
}
