package Giuseppe.DigitalDelights.address;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "addressId")
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
	@JsonBackReference
	private User user;

	public Address(String via, int numeroCivico, String località, String cap, String comune, User user) {
		this.via = via;
		this.numeroCivico = numeroCivico;
		this.località = località;
		this.cap = cap;
		this.comune = comune;
		this.user = user;
	}

}
