package Giuseppe.DigitalDelights.prodotti;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
