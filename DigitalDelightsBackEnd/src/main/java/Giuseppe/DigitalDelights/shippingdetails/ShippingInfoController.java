package Giuseppe.DigitalDelights.shippingdetails;

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
@RequestMapping("/shipping")
public class ShippingInfoController {

	private final ShippingInfoService shippingSrv;

	@Autowired
	public ShippingInfoController(ShippingInfoService shippingSrv) {
		this.shippingSrv = shippingSrv;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<ShippingInfo> getShippingInfos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "shippingInfoId") String sortBy) {
		return shippingSrv.find(page, size, sortBy);
	}

	@GetMapping("/{shippingInfoId}")
	public ShippingInfo findById(@PathVariable UUID shippingInfoId) {
		return shippingSrv.findById(shippingInfoId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ShippingInfo saveShippingInfo(@RequestBody ShippingInfoRequestPayload body) {
		return shippingSrv.create(body);
	}

	@PutMapping("/{shippingInfoId}")
	public ShippingInfo updateShippingInfo(@PathVariable UUID shippingInfoId,
			@RequestBody ShippingInfoRequestPayload body) {
		return shippingSrv.findByIdAndUpdate(shippingInfoId, body);
	}

	@DeleteMapping("/{shippingInfoId}")
	public ResponseEntity<String> deleteShippingInfo(@PathVariable UUID shippingInfoId) {
		shippingSrv.findByIdAndDelete(shippingInfoId);
		return ResponseEntity.ok("Informazioni di spedizione eliminate con successo.");
	}
}
