package Giuseppe.DigitalDelights.reviews;

import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.user.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewsRequestPayload {

	@Min(0)
	@Max(5)
	private int rating;

	private String reviewText;

	private User user;

	private Product product;

}
