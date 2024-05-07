package py.com.seto.google.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()  // Deshabilitar CSRF
				.authorizeHttpRequests(auth -> auth
						.anyRequest().authenticated()  // Requiere autenticación para cualquier solicitud
				)
				.oauth2Login()  // Habilitar el login OAuth2
				.and()
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))  // Permite logout mediante GET
						.addLogoutHandler(new CookieClearingLogoutHandler("JSESSIONID"))  // Específica la cookie a eliminar
						.addLogoutHandler(new SecurityContextLogoutHandler())  // Cierra sesión de Spring Security
						.logoutSuccessUrl("/") // Página a la que redirigir después del logout
						.clearAuthentication(true)  // Limpia la autenticación
						.invalidateHttpSession(true)  // Invalida la sesión HTTP
						.deleteCookies("JSESSIONID")
				);

		return http.build();
	}
}

