package Giuseppe.DigitalDelights.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, UUID> {
	List<Reviews> findAllByProduct_productId(UUID productId);
}
