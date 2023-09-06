package Giuseppe.DigitalDelights.reviews;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;

@Service
public class ReviewsService {
	private final ReviewsRepository reviewsRepo;

	@Autowired
	public ReviewsService(ReviewsRepository reviewsRepo) {
		this.reviewsRepo = reviewsRepo;
	}

	public Reviews create(ReviewsRequestPayload body) {
		Reviews newReviews = new Reviews(body.getRating(), body.getReviewText(), body.getUser(), body.getProduct());
		return reviewsRepo.save(newReviews);
	}

	public Page<Reviews> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return reviewsRepo.findAll(pageable);
	}

	public Reviews findById(UUID id) throws NotFoundException {
		return reviewsRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Reviews findByIdAndUpdate(UUID id, ReviewsRequestPayload body) throws NotFoundException {
		Reviews found = this.findById(id);
		found.setRating(body.getRating());
		found.setReviewText(body.getReviewText());
		found.setUser(body.getUser());
		found.setProduct(body.getProduct());

		return reviewsRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Reviews found = this.findById(id);
		reviewsRepo.delete(found);
	}
}
