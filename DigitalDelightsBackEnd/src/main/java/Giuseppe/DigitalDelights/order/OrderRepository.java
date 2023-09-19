package Giuseppe.DigitalDelights.order;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Giuseppe.DigitalDelights.user.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
	List<Order> findAllByUser(User user);
}
