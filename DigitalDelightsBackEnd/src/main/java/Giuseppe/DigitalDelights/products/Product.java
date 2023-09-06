package Giuseppe.DigitalDelights.products;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import Giuseppe.DigitalDelights.reviews.Reviews;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue
	private UUID productId;

	private String name;

	private String description;

	private double price;

	private String imageUrl;

	private boolean active;

	private int unitsInStock;

	@CreationTimestamp
	private Date dateCreated;
	@UpdateTimestamp
	private Date lastUpdated;

	@Enumerated(EnumType.STRING)
	private Category category;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Reviews> reviews = new ArrayList<>();

	public Product(String name, String description, double price, String imageUrl, boolean active, int unitsInStock,
			Category category) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.imageUrl = imageUrl;
		this.active = active;
		this.unitsInStock = unitsInStock;
		this.category = category;
	}
}
