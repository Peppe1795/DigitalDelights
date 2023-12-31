package Giuseppe.DigitalDelights.address;

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

@RestController
@RequestMapping("/indirizzo")
public class AddressController {
	private final AddressService addressSrv;

	@Autowired
	public AddressController(AddressService addressSrv) {
		this.addressSrv = addressSrv;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<Address> getAddress(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "addressId") String sortBy) {
		return addressSrv.find(page, size, sortBy);
	}

	@GetMapping("/{addressId}")
	public Address findById(@PathVariable UUID addressId) {
		return addressSrv.findById(addressId);

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Address saveAddress(@RequestBody AddressRequestPayload body) {
		Address created = addressSrv.create(body);

		return created;
	}

	@PutMapping("/{addressId}")
	public Address updateAddress(@PathVariable UUID addressId, @RequestBody AddressRequestPayload body) {
		return addressSrv.findByIdAndUpdate(addressId, body);
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable UUID addressId) {
		addressSrv.findByIdAndDelete(addressId);
		return ResponseEntity.ok("Indirizzo eliminato con successo.");

	}
}
