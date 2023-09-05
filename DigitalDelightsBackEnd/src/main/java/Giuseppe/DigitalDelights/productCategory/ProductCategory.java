package Giuseppe.DigitalDelights.productCategory;

import java.util.Set;
import java.util.UUID;

import Giuseppe.DigitalDelights.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_category")
@Getter
@Setter
public class ProductCategory {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID id;
	@Column(name = "category_name")
	private String categoryName;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	private Set<Product> products;
}
