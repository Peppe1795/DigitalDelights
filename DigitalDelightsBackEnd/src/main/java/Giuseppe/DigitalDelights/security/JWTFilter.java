package Giuseppe.DigitalDelights.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import Giuseppe.DigitalDelights.exception.UnauthorizedException;
import Giuseppe.DigitalDelights.user.User;
import Giuseppe.DigitalDelights.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	JWTTools jwttools;
	@Autowired
	UserService uS;

	private static final String[] PUBLIC_GET_ROUTES = { "/product", "/reviews", "/product/{productId}" };
	private static final String[] ADMIN_ROUTES = { "/product/**", "/reviews/**" };

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String servletPath = request.getServletPath();

		// Controlla se la richiesta è una richiesta GET per una route pubblica
		if (isPublicGetRoute(servletPath) && request.getMethod().equals("GET")) {
			filterChain.doFilter(request, response); // Passa direttamente alla catena di filtri successiva
			return;
		}

		// Altrimenti, richiedi il token JWT
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer "))
			throw new UnauthorizedException("Per favore passa il token nell'authorization header");
		String token = authHeader.substring(7);
		System.out.println("TOKEN = " + token);
		jwttools.verificaToken(token);
		String id = jwttools.extractSubject(token);
		User currentUser = uS.findById(UUID.fromString(id));

		// Verifica il ruolo dell'utente
		if (isAdminRoute(servletPath) && !currentUser.getRole().equals("ADMIN")) {
			throw new UnauthorizedException("L'accesso a questa route richiede il ruolo di admin");
		}

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(currentUser, null,
				currentUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		System.out.println(request.getServletPath());
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}

	// Verifica se la route è pubblica per le richieste GET
	private boolean isPublicGetRoute(String servletPath) {
		for (String publicRoute : PUBLIC_GET_ROUTES) {
			if (new AntPathMatcher().match(publicRoute, servletPath)) {
				return true;
			}
		}
		return false;
	}

	// Verifica se la route richiede il ruolo di admin
	private boolean isAdminRoute(String servletPath) {
		for (String adminRoute : ADMIN_ROUTES) {
			if (new AntPathMatcher().match(adminRoute, servletPath)) {
				return true;
			}
		}
		return false;
	}
}
