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
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<Order> listOrders(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "orderId") String sortBy) {
		return orderService.find(page, size, sortBy);
	}

	@GetMapping("/{orderId}")
	public Order getOrderById(@PathVariable UUID orderId) {
		return orderService.findById(orderId);
	}

	@PutMapping("/{orderId}/status")
	public Order updateOrderStatus(@PathVariable UUID orderId, @RequestBody StatusRequestPayload statusPayload) {
		return orderService.updateOrderStatus(orderId, statusPayload.getStatus());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Order createOrder(@RequestBody OrderRequestPayload body) {
		return orderService.create(body);
	}

	@PutMapping("/{orderId}")
	public Order updateOrder(@PathVariable UUID orderId, @RequestBody OrderRequestPayload body) {
		return orderService.findByIdAndUpdate(orderId, body);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {
		orderService.findByIdAndDelete(orderId);
		return ResponseEntity.ok("Order successfully deleted.");
	}
}
