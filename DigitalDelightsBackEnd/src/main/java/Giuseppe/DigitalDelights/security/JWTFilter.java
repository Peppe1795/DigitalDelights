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
	JWTTools jTools;
	@Autowired
	UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer "))
			throw new UnauthorizedException("Inserisci il token nell'autorization Header");
		String token = header.substring(7);
		System.out.println("Token: -> " + token);

		jTools.verificaToken(token);
		String id = jTools.extractSubject(token);
		User utenteCorrente = userService.findById(UUID.fromString(id));

		UsernamePasswordAuthenticationToken autorizzationToken = new UsernamePasswordAuthenticationToken(utenteCorrente,
				null, utenteCorrente.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(autorizzationToken);

		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		System.out.println(request.getServletPath());
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
