package Giuseppe.DigitalDelights.address;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Giuseppe.DigitalDelights.exception.NotFoundException;

@Service
public class AddressService {

	private final AddressRepository addressRepo;

	@Autowired
	public AddressService(AddressRepository addressRepo) {
		this.addressRepo = addressRepo;
	}

	public Address create(AddressRequestPayload body) {
		Address newAddress = new Address(body.getVia(), body.getNumeroCivico(), body.getLocalità(), body.getComune(),
				body.getCap(), body.getUser());
		return addressRepo.save(newAddress);
	}

	public Page<Address> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return addressRepo.findAll(pageable);
	}

	public Address findById(UUID id) throws NotFoundException {
		return addressRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Address findByIdAndUpdate(UUID id, AddressRequestPayload body) throws NotFoundException {
		Address found = this.findById(id);
		found.setVia(body.getVia());
		found.setNumeroCivico(body.getNumeroCivico());
		found.setLocalità(body.getLocalità());
		found.setCap(body.getCap());

		return addressRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Address found = this.findById(id);
		addressRepo.delete(found);
	}
}
