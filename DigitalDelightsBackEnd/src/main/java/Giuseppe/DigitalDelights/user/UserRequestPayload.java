package Giuseppe.DigitalDelights.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestPayload {
	@NotNull(message = "Il campo nome è obbligatorio")
	@Size(min = 4, max = 20, message = "Nome deve avere minimo 3 caratteri, massimo 30")
	private String name;
	@NotNull(message = "Il campo username è obbligatorio")
	private String username;
	@NotNull(message = "Il campo cognome è obbligatorio")
	private String lastName;
	@NotNull(message = "Inserisci una email valida, quest campo non può rimanere vuoto")
	@Email(message = "L'email inserita non è un indirizzo valido")
	private String email;
	@NotNull(message = "La password è obbligatoria")
	private String password;
	private Role role;
}
