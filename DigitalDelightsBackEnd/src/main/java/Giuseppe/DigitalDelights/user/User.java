package Giuseppe.DigitalDelights.user;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import Giuseppe.DigitalDelights.address.Address;
import Giuseppe.DigitalDelights.order.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "password", "accountNonExpired", "authorities", "credentialsNonExpired", "accountNonLocked" })
public class User implements UserDetails {
	@Id
	@GeneratedValue
	private UUID userId;
	private String username;
	private String name;
	private String lastName;
	@Column(nullable = true, unique = true)
	private String email;
	private String password;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;
	@Enumerated(EnumType.STRING)
	private Role role;
	@OneToMany(mappedBy = "user")
	private List<Order> order;

	public User(String username, String name, String lastName, String email, String password, Address address,
			Role role) {

		this.username = username;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));

	}

	@Override
	public String getUsername() {

		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

}
