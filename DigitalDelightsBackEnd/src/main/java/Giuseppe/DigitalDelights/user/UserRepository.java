package Giuseppe.DigitalDelights.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Giuseppe.DigitalDelights.products.Product;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	Optional<User> findByName(String name);

	@Query("SELECT p FROM User u JOIN u.favoriteProducts p WHERE u.userId = ?1")
	Page<Product> findFavoriteProductsByUserId(UUID userId, Pageable pageable);

	List<User> findAllByFavoriteProductsContaining(Product product);
}
