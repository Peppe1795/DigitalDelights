package Giuseppe.DigitalDelights.products;

import java.util.UUID;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
	Page<Product> findByNameContainingIgnoreCase(String parteDelNome, Pageable page);

	@Query("SELECT p FROM Product p WHERE " + "(p.name LIKE CONCAT('%', :name, '%') OR :name IS NULL) AND "
			+ "(p.category = :category OR :category IS NULL) AND " + "(p.price >= :minPrice OR :minPrice IS NULL) AND "
			+ "(p.price <= :maxPrice OR :maxPrice IS NULL)")
	Page<Product> findFilteredProducts(@Param("name") String name, @Param("category") Category category,
			@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
}
