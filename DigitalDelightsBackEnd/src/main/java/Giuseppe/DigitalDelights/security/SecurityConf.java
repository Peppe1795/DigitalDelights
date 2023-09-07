package Giuseppe.DigitalDelights.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConf {
	@Autowired
	JWTFilter jwtFilter;

	@Autowired
	CorsFilter corsFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(c -> c.disable());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/product/**").authenticated());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/reviews/**").authenticated());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/order/**").authenticated());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/cart/**").authenticated());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/indirizzo/**").authenticated());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/user/**").authenticated());

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(corsFilter, JWTFilter.class);

		return http.build();
	}

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(12);
	}

}
