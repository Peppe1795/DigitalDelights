package Giuseppe.DigitalDelights.prodotti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequestPayload {
	private String name;

	private String description;

	private double price;

	private String imageUrl;

	private boolean active;

	private int unitsInStock;

	private Category category;
}
