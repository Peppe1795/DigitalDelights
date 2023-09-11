package Giuseppe.DigitalDelights.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;
import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.products.ProductRepository;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserRepository;
import Giuseppe.DigitalDelights.user.UserService;

@Service
public class ReviewsService {
	private final ReviewsRepository reviewsRepo;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserService us;

	@Autowired
	public ReviewsService(ReviewsRepository reviewsRepo) {
		this.reviewsRepo = reviewsRepo;
	}

	public Reviews createReviewForProduct(UUID productId, int rating, String reviewText) {
		User currentUser = us.getCurrentUser();
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundException("L'id non corrissponde a nessun prodotto " + productId));

		Reviews review = new Reviews();
		review.setUser(currentUser);
		review.setProduct(product);
		review.setRating(rating);
		review.setReviewText(reviewText);

		return reviewsRepo.save(review);
	}

	public List<Reviews> getReviewsForProduct(UUID productId) {
		return reviewsRepo.findAllByProduct_productId(productId);
	}

	public Reviews findById(UUID id) throws NotFoundException {
		return reviewsRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Reviews findByIdAndUpdate(UUID id, ReviewsRequestPayload body) throws NotFoundException {
		Reviews found = this.findById(id);
		found.setRating(body.getRating());
		found.setReviewText(body.getReviewText());

		return reviewsRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Reviews found = this.findById(id);
		reviewsRepo.delete(found);
	}

}
