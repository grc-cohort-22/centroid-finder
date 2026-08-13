package io.github.konradkelly.centroidfinder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Authentication is scoped to the one endpoint that costs something to call.
 *
 * <p>{@code POST /process/**} launches a separate JVM per job (see
 * {@link DefaultJobProcessLauncher}) against a pool of 4-8 threads with a queue
 * of 100. Left open, anyone can queue a hundred JVM launches, which is a cost
 * and availability problem rather than a data one. Everything else — listing
 * videos, thumbnails, job status, and the result CSVs — stays public: the
 * browser frontend reads those on page load and cannot hold a credential
 * without publishing it, and the data itself is animal coordinates.
 *
 * <p>Requests are stateless: no session is created, so each POST carries its own
 * Basic credentials. CSRF protection is disabled because there is no cookie or
 * session for an attacker to ride — the protection it offers does not apply to
 * a credential sent explicitly on every request.
 */
@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityConfig {

    private String username;

    /**
     * A BCrypt hash, never a plaintext password. Configured per environment:
     * {@code application.yml} carries a hash of the documented local-development
     * credential, and {@code application-prod.yml} requires
     * {@code APP_SECURITY_PASSWORD_HASH} with no default, so a production start
     * fails fast rather than falling back to something guessable.
     */
    private String passwordHash;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/process/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        if (username == null || username.isBlank() || passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalStateException(
                "app.security.username and app.security.password-hash must both be set. "
                + "In production supply APP_SECURITY_USERNAME and APP_SECURITY_PASSWORD_HASH; "
                + "the hash is BCrypt, not a plaintext password."
            );
        }
        return new InMemoryUserDetailsManager(
            User.withUsername(username)
                .password(passwordHash)
                .roles("USER")
                .build()
        );
    }
}
