package br.com.fcamara.controleveiculos.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import br.com.fcamara.controleveiculos.config.jwt.AuthEntryPointJwt;
import br.com.fcamara.controleveiculos.config.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    private static final String[] WHITE_LIST_URL = { "/v3/api-docs/**", "/swagger-resources/**", "/configuration/ui","/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", 
    		"/h2-console/**", "/signin", "/signup", "/graphiql/**", "/graphql/**"};
    

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())  // Pode ser configurado explicitamente se necessário
            .authorizeHttpRequests(req -> req
            	.requestMatchers("/auth/**").permitAll()
                .requestMatchers(WHITE_LIST_URL).permitAll()  // Permite acesso aos caminhos da lista branca
                .anyRequest().authenticated())  // Exige autenticação para outros caminhos
            .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))  // Custom exception handler
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))  // Sem sessões
            .authenticationProvider(authenticationProvider())  // Provedor de autenticação
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);  // Filtro JWT

        return http.build();
    }

}