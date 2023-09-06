package Giuseppe.DigitalDelights.reviews;

import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewsRequestPayload {

	private int rating;

	private String reviewText;

	private User user;

	private Product product;

}
