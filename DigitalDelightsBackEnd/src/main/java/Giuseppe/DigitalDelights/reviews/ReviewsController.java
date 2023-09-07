package Giuseppe.DigitalDelights.reviews;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

	@GetMapping
	public Page<Reviews> getReviews(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "reviewsId") String sortBy) {
		return reviewsSrv.find(page, size, sortBy);
	}

	@GetMapping("/{reviewsId}")
	public Reviews findById(@PathVariable UUID reviewsId) {
		return reviewsSrv.findById(reviewsId);

	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public Reviews saveReviews(@Valid @RequestBody ReviewsRequestPayload body) {
		Reviews created = reviewsSrv.create(body);

		return created;
	}

	@PutMapping("/{reviewsId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public Reviews updateReviews(@PathVariable UUID reviewsId, @Valid @RequestBody ReviewsRequestPayload body) {
		return reviewsSrv.findByIdAndUpdate(reviewsId, body);
	}

	@DeleteMapping("/{reviewsId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteReviews(@PathVariable UUID reviewsId) {
		reviewsSrv.findByIdAndDelete(reviewsId);
		return ResponseEntity.ok("Recensione eliminata con successo.");

	}
}
