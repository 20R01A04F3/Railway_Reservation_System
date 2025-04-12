package com.example.config;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.filter.JwtFilter;
import com.example.service.CustomerUserDetailsService;
@Configuration
@EnableWebSecurity
public class SpringConfiguration { 

	@Autowired
	 JwtFilter authFilter; 
	

	@Autowired
	UserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
		
		http
      .csrf((csrf) -> csrf.disable())
      .headers((frameOptions)-> frameOptions.disable())
      .authorizeHttpRequests((authz) -> authz 
   
	    		.requestMatchers("/api/auth/**").permitAll()
	    		.requestMatchers("/v3/api-docs/**", "/swagger-ui/**")
	    		.permitAll()
				.requestMatchers("/passenger/**")
				.hasAnyRole("ADMIN","USER") 
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				
	    		);
     http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
     http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

   
    
		return http.build();
	} 

	
	
	
	public AuthenticationProvider authenticationProvider() { 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
		authenticationProvider.setUserDetailsService(userDetailsService); 
		authenticationProvider.setPasswordEncoder(passwordEncoder()); 
		return authenticationProvider; 
	} 

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
		return config.getAuthenticationManager(); 
	} 

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Default strength is 10
    }
}
