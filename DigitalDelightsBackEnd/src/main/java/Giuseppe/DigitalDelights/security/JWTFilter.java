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

	private static final String[] PUBLIC_ROUTES = { "/product/**", "/reviews/**" };

	private static final String[] USER_ROUTES = { "/{cartId}/product/{productId}", "/current-user-cart-id",
			"/cart/{cartId}", "/cart/{cartId}/products", "/addWishList/{productId}", "/removeWishList/{productId}",
			"/{userId}/wishList", "user/{userId}", "/orders", "/reviews", "/reviews/*", "/orders/*" };

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String servletPath = request.getServletPath();

		if (isPublicRoute(servletPath)) {
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new UnauthorizedException("Per favore passa il token nell'authorization header");
		}

		String token = authHeader.substring(7);
		jwttools.verificaToken(token);
		String roleFromToken = jwttools.extractRole(token);

		if ("ADMIN".equalsIgnoreCase(roleFromToken)) {
			setAuthentication(token);
			filterChain.doFilter(request, response);
			return;
		}

		if (isUserRoute(servletPath) && !"USER".equalsIgnoreCase(roleFromToken)) {
			throw new UnauthorizedException("L'accesso a questa route richiede il ruolo di user");
		}

		setAuthentication(token);
		filterChain.doFilter(request, response);
	}

	private void setAuthentication(String token) {
		String id = jwttools.extractSubject(token);
		User currentUser = uS.findById(UUID.fromString(id));
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(currentUser, null,
				currentUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		System.out.println("Checking shouldNotFilter for: " + request.getServletPath());
		return request.getServletPath().startsWith("/auth");
	}

	private boolean isPublicRoute(String servletPath) {
		for (String publicRoute : PUBLIC_ROUTES) {
			if (new AntPathMatcher().match(publicRoute, servletPath)) {
				return true;
			}
		}
		return false;
	}

	private boolean isUserRoute(String servletPath) {
		for (String userRoute : USER_ROUTES) {
			if (new AntPathMatcher().match(userRoute, servletPath)) {
				return true;
			}
		}
		return false;
	}

}