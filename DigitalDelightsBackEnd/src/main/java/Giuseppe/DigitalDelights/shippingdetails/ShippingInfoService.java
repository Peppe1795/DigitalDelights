package Giuseppe.DigitalDelights.shippingdetails;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;

@Service
public class ShippingInfoService {

	private final ShippingInfoRepository shippingRepo;

	@Autowired
	public ShippingInfoService(ShippingInfoRepository shippingRepo) {
		this.shippingRepo = shippingRepo;
	}

	public ShippingInfo create(ShippingInfoRequestPayload body) {
		ShippingInfo newShippingInfo = body.toShippingInfo();
		return shippingRepo.save(newShippingInfo);
	}

	public Page<ShippingInfo> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return shippingRepo.findAll(pageable);
	}

	public ShippingInfo findById(UUID id) throws NotFoundException {
		return shippingRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("ShippingInfo not found with ID: " + id));
	}

	public ShippingInfo findByIdAndUpdate(UUID id, ShippingInfoRequestPayload body) throws NotFoundException {
		ShippingInfo found = this.findById(id);
		ShippingInfo updatedShippingInfo = body.toShippingInfo();

		// Updating the found entity with new values
		found.setRecipientName(updatedShippingInfo.getRecipientName());
		found.setShippingAddress(updatedShippingInfo.getShippingAddress());
		found.setCity(updatedShippingInfo.getCity());
		found.setState(updatedShippingInfo.getState());
		found.setPostalCode(updatedShippingInfo.getPostalCode());
		found.setCountry(updatedShippingInfo.getCountry());
		found.setPhoneNumber(updatedShippingInfo.getPhoneNumber());

		return shippingRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		ShippingInfo found = this.findById(id);
		shippingRepo.delete(found);
	}
}
