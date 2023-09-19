package Giuseppe.DigitalDelights.order;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;
import Giuseppe.DigitalDelights.orderitem.OrderItem;
import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.products.ProductRepository;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserService;

@Service
public class OrderService {

	private final OrderRepository orderRepo;
	private final ProductRepository productRepo;
	@Autowired
	private UserService userService; // Usa direttamente UserService invece di UserRepository

	@Autowired
	public OrderService(OrderRepository orderRepo, ProductRepository productRepo) {
		this.orderRepo = orderRepo;
		this.productRepo = productRepo;
	}

	public Order create(OrderRequestPayload body) {
		User currentUser = userService.getCurrentUser(); // Usa direttamente il metodo da UserService

		List<OrderItem> orderItems = body.getOrderItems().stream().map(itemPayload -> {
			Product product = productRepo.findById(itemPayload.getProductId()).orElseThrow(
					() -> new NotFoundException("Prodotto non trovato con ID: " + itemPayload.getProductId()));
			return itemPayload.toOrderItem(product);
		}).collect(Collectors.toList());

		Order newOrder = new Order(currentUser, Status.PENDING, body.getTotalPrice(),
				body.getShippingInfoRequestPayload().toShippingInfo(), orderItems);

		return orderRepo.save(newOrder);
	}

	public Page<Order> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return orderRepo.findAll(pageable);
	}

	public List<Order> findOrdersByUser(User user) {
		return orderRepo.findAllByUser(user);
	}

	public Order findById(UUID id) throws NotFoundException {
		return orderRepo.findById(id).orElseThrow(() -> new NotFoundException("Ordine non trovato con ID: " + id));
	}

	public Order findByIdAndUpdate(UUID id, OrderRequestPayload body) throws NotFoundException {
		Order foundOrder = this.findById(id);

		List<OrderItem> orderItems = body.getOrderItems().stream().map(itemPayload -> {
			Product product = productRepo.findById(itemPayload.getProductId()).orElseThrow(
					() -> new NotFoundException("Prodotto non trovato con ID: " + itemPayload.getProductId()));
			return itemPayload.toOrderItem(product);
		}).collect(Collectors.toList());

		foundOrder.setUser(userService.getCurrentUser()); // Setta l'utente corrente
		foundOrder.setTotalPrice(body.getTotalPrice());
		foundOrder.setShippingInfo(body.getShippingInfoRequestPayload().toShippingInfo());
		foundOrder.setOrderItems(orderItems);

		return orderRepo.save(foundOrder);
	}

	public Order updateOrderStatus(UUID id, Status newStatus) throws NotFoundException {
		Order foundOrder = this.findById(id);
		foundOrder.setStatus(newStatus);
		return orderRepo.save(foundOrder);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Order foundOrder = this.findById(id);
		orderRepo.delete(foundOrder);
	}
}
