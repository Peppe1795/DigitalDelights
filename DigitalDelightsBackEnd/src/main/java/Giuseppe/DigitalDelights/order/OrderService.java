package Giuseppe.DigitalDelights.order;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;

@Service
public class OrderService {
	private final OrderRepository orderSrv;

	@Autowired
	public OrderService(OrderRepository orderSrv) {
		this.orderSrv = orderSrv;
	}

	public Order create(OrderRequestPayload body) {
		Order newOrder = new Order(body.getUser(), body.getProducts(), body.getOrderDate(), body.getTotalPrice());
		return orderSrv.save(newOrder);
	}

	public Page<Order> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return orderSrv.findAll(pageable);
	}

	public Order findById(UUID id) throws NotFoundException {
		return orderSrv.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Order findByIdAndUpdate(UUID id, OrderRequestPayload body) throws NotFoundException {
		Order found = this.findById(id);
		found.setUser(body.getUser());
		found.setProducts(body.getProducts());
		found.setOrderDate(body.getOrderDate());
		found.setTotalPrice(body.getTotalPrice());

		return orderSrv.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Order found = this.findById(id);
		orderSrv.delete(found);
	}
}
