package Giuseppe.DigitalDelights.order;

import java.util.ArrayList;
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
	private UserService userService; 
	@Autowired
	private EmailService emailService;

	@Autowired
	public OrderService(OrderRepository orderRepo, ProductRepository productRepo) {
		this.orderRepo = orderRepo;
		this.productRepo = productRepo;
	}

	public Order create(OrderRequestPayload body) {
		User currentUser = userService.getCurrentUser();

		Order initialOrder = new Order(currentUser, Status.DELIVERED, body.getTotalPrice(),
				body.getShippingInfoRequestPayload().toShippingInfo(), new ArrayList<>());

		final Order[] savedOrder = new Order[1];
		savedOrder[0] = orderRepo.save(initialOrder);

		List<OrderItem> orderItems = body.getOrderItems().stream().map(itemPayload -> {
			Product product = productRepo.findById(itemPayload.getProductId()).orElseThrow(
					() -> new NotFoundException("Prodotto non trovato con ID: " + itemPayload.getProductId()));

			return new OrderItem(savedOrder[0], product, itemPayload.getQuantity());
		}).collect(Collectors.toList());

		savedOrder[0].setOrderItems(orderItems);

		return orderRepo.save(savedOrder[0]);
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
	public DetailedOrderResponse findDetailedOrderById(UUID id) throws NotFoundException {
	    Order order = this.findById(id);
	    
	    List<Product> products = order.getOrderItems().stream().map(OrderItem::getProduct).collect(Collectors.toList());

	    return new DetailedOrderResponse(order.getOrderId(), order.getStatus(), order.getTotalPrice(), order.getShippingInfo(), products);
	}
	
	

	public Order findByIdAndUpdate(UUID id, OrderRequestPayload body) throws NotFoundException {
		Order foundOrder = this.findById(id);

		List<OrderItem> orderItems = body.getOrderItems().stream().map(itemPayload -> {
			Product product = productRepo.findById(itemPayload.getProductId()).orElseThrow(
					() -> new NotFoundException("Prodotto non trovato con ID: " + itemPayload.getProductId()));
			return itemPayload.toOrderItem(product);
		}).collect(Collectors.toList());

		foundOrder.setUser(userService.getCurrentUser());
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
	public Order shipOrder(UUID id) throws NotFoundException {
	    Order foundOrder = this.findById(id);
	    foundOrder.setStatus(Status.SHIPPED);
	    orderRepo.save(foundOrder);

	   
	    String userEmail = foundOrder.getUser().getEmail();
	    String orderId = foundOrder.getOrderId().toString();
	    List<String> productNames = foundOrder.getOrderItems().stream()
	            .map(item -> item.getProduct().getName() + " (Quantità: " + item.getQuantity() + ")")
	            .collect(Collectors.toList());
	    String productsList = String.join(", ", productNames);
	    String shippingAddress = foundOrder.getShippingInfo().toString(); 

	  
	    String emailBody = String.format(
	            "Gentile cliente,\n\nIl suo ordine con ID %s è stato spedito.\n\nProdotti nel suo ordine: %s\n\nDettagli della spedizione:\n%s\n\nGrazie per aver fatto acquisti con noi.\n\nCordiali saluti,\nIl Team di Giuseppe's Digital Delights",
	            orderId, productsList, shippingAddress
	    );


	    emailService.sendSimpleMessage(userEmail, "Il tuo ordine è stato spedito", emailBody);

	    return foundOrder;
	}

}
