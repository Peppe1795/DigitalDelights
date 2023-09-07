package Giuseppe.DigitalDelights.order;

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
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderSrv;

	@Autowired
	public OrderController(OrderService orderSrv) {
		this.orderSrv = orderSrv;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<Order> getProduct(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "orderId") String sortBy) {
		return orderSrv.find(page, size, sortBy);
	}

	@GetMapping("/{orderId}")
	public Order findById(@PathVariable UUID orderId) {
		return orderSrv.findById(orderId);

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Order saveIndirizzo(@RequestBody OrderRequestPayload body) {
		Order created = orderSrv.create(body);

		return created;
	}

	@PutMapping("/{orderId}")
	public Order updateProduct(@PathVariable UUID orderId, @RequestBody OrderRequestPayload body) {
		return orderSrv.findByIdAndUpdate(orderId, body);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> deleteProduct(@PathVariable UUID orderId) {
		orderSrv.findByIdAndDelete(orderId);
		return ResponseEntity.ok("Ordine eliminato con successo.");

	}
}
