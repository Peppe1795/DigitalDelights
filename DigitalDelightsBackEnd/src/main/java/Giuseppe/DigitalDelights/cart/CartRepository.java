package Giuseppe.DigitalDelights.cart;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
	// Optional<Cart> findByUserId(UUID userId);
}
