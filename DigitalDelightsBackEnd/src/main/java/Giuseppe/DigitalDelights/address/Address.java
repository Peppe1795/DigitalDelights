package Giuseppe.DigitalDelights.address;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Giuseppe.DigitalDelights.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
	private String numeroCivico;
	private String località;
	private int cap;

	@JsonIgnore
	@ManyToOne
	private User user;

	public Address(String via, String numeroCivico, String località, int cap, User user) {

		this.via = via;
		this.numeroCivico = numeroCivico;
		this.località = località;
		this.cap = cap;
		this.user = user;
	}

}
