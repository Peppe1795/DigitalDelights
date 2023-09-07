package Giuseppe.DigitalDelights.reviews;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
public class Reviews {
	@Id
	@GeneratedValue
	private UUID reviewId;

	@Column(nullable = false)
	@Min(0)
	@Max(5)
	private int rating;

	@Column(length = 1024)
	private String reviewText;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "product_id")
	private Product product;

	@CreationTimestamp
	private Date dateCreated;

	@UpdateTimestamp
	private Date lastUpdated;

	public Reviews(int rating, String reviewText, User user, Product product) {
		this.rating = rating;
		this.reviewText = reviewText;
		this.user = user;
		this.product = product;
	}

}
