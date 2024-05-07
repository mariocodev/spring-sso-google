package py.com.seto.google.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;

@SpringBootApplication
@RestController
public class SpringSsoGoogleApplication {

	@GetMapping
	public ResponseEntity<String> hello(OAuth2AuthenticationToken token){
		if (token != null) {
			// Obtener el nombre, apellido y email del usuario
			String givenName = token.getPrincipal().getAttribute("given_name");
			String familyName = token.getPrincipal().getAttribute("family_name");
			String email = token.getPrincipal().getAttribute("email");

			// Crear un mensaje con la información del usuario
			String userInfo = String.format("Hello %s %s (email: %s)", givenName, familyName, email);
			return ResponseEntity.ok(userInfo);
		}

		return ResponseEntity.ok("Hello from secure endpoint");
	}

	@GetMapping("/user")
	public Principal user (Principal principal){
		return principal;
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();  // Invalidar sesión
		}

		// Limpiar cualquier cookie que pueda ser parte de la sesión
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}

		return "redirect:/";  // Redireccionar a la página de login o home
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringSsoGoogleApplication.class, args);
	}

}
