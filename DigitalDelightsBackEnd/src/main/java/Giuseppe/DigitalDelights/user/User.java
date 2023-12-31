package Giuseppe.DigitalDelights.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import Giuseppe.DigitalDelights.address.Address;
import Giuseppe.DigitalDelights.cart.Cart;
import Giuseppe.DigitalDelights.order.Order;
import Giuseppe.DigitalDelights.products.Product;
import Giuseppe.DigitalDelights.reviews.Reviews;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "password", "accountNonExpired", "authorities", "credentialsNonExpired", "accountNonLocked" })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
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
	@JsonManagedReference
	private Address address;
	@Enumerated(EnumType.STRING)
	private Role role;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> order;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Cart cart;

	@ManyToMany
	@JoinTable(name = "user_prodotti_preferiti", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> favoriteProducts = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Reviews> reviews = new ArrayList<>();

	public User(String name, String username, String lastName, String email, String password, Address address,
			Role role) {
		this.name = name;
		this.username = username;

		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.role = role;
		this.favoriteProducts = new ArrayList<>();
		this.reviews = new ArrayList<>();
		this.cart = new Cart(this);
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
