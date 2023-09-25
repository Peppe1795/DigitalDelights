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

	 Page<Product> findByCategory(Category category, Pageable pageable);
}
