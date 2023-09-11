package Giuseppe.DigitalDelights.reviews;

import java.util.UUID;

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

	private UUID userId;

	private UUID productId;

}
