package Giuseppe.DigitalDelights.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

	private final ReviewsService reviewsSrv;

	@Autowired
	public ReviewsController(ReviewsService reviewsSrv) {
		this.reviewsSrv = reviewsSrv;
	}

	@GetMapping("/{reviewId}")
	public Reviews getReviewById(@PathVariable UUID reviewId) {
		return reviewsSrv.findById(reviewId);
	}

	 @PostMapping("/product/{productId}/user/{userId}")
	    public Reviews addReviewToProduct(
	        @PathVariable UUID userId,
	        @PathVariable UUID productId,
	        @RequestBody @Valid ReviewsRequestPayload payload
	    ) {
	        return reviewsSrv.createReviewForProduct(userId, productId, payload.getRating(), payload.getReviewText());
	    }

	@GetMapping("/product/{productId}")
	public List<Reviews> getReviewsForProduct(@PathVariable UUID productId) {
		return reviewsSrv.getReviewsForProduct(productId);
	}

	@PutMapping("/{reviewId}")
	public Reviews updateReview(@PathVariable UUID reviewId, @RequestBody @Valid ReviewsRequestPayload payload) {
		return reviewsSrv.findByIdAndUpdate(reviewId, payload);
	}

	@DeleteMapping("/{reviewId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteReview(@PathVariable UUID reviewId) {
		reviewsSrv.findByIdAndDelete(reviewId);
		return ResponseEntity.ok("Recensione eliminata con successo.");
	}
}
