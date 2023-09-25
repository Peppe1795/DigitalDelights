package Giuseppe.DigitalDelights.order;

import java.util.List;
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

import Giuseppe.DigitalDelights.exception.NotFoundException;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;
	private final UserService userService;

	@Autowired
	public OrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<Order> listOrders(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "orderId") String sortBy) {
		return orderService.find(page, size, sortBy);
	}

	@GetMapping("/my-orders")
	public List<Order> getMyOrders() {
		User currentUser = userService.getCurrentUser();
		return orderService.findOrdersByUser(currentUser);
	}
	@GetMapping("/{orderId}/details")
	public DetailedOrderResponse getOrderDetails(@PathVariable UUID orderId) {
	    return orderService.findDetailedOrderById(orderId);
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
	
	@PutMapping("/{orderId}/ship")
	public Order shipOrder(@PathVariable UUID orderId) throws NotFoundException {
	    return orderService.shipOrder(orderId);
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
