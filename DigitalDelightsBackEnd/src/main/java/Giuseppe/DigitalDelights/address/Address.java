package Giuseppe.DigitalDelights.address;

import java.util.UUID;

import Giuseppe.DigitalDelights.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Address")
@Data
@NoArgsConstructor
public class Address {
	@Id
	@GeneratedValue
	private UUID addressId;
	private String via;
	private int numeroCivico;
	private String località;
	private String cap;
	private String comune;

	@OneToOne(mappedBy = "address")
	@JoinColumn(name = "user_id")
	private User user;

	public Address(String via, int numeroCivico, String località, String cap, String comune, User user) {
		super();
		this.via = via;
		this.numeroCivico = numeroCivico;
		this.località = località;
		this.cap = cap;
		this.comune = comune;
		this.user = user;
	}

}
