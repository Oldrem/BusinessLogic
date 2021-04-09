package app.security;

import app.repositories.UserRepository;
import app.security.jwt.JwtAuthenticationFilter;
import app.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserDetailsProvider userDetails;

    private UserRepository users;


    public SecurityConfig(UserDetailsProvider userDetailsProvider, UserRepository users) {
        this.userDetails = userDetailsProvider;
        this.users = users;
    }


    @Bean
    public BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetails)
                .passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/products", "/api/product/**").permitAll() // Anyone can view products
                .antMatchers(HttpMethod.POST, "/api/order").permitAll() // Anyone can create a new order
                .antMatchers(HttpMethod.POST, "/api/login").permitAll() // Anyone can login lol

                .antMatchers(HttpMethod.GET, "/api/orders", "/api/order/**").hasRole("STOREFRONT")
                .antMatchers(HttpMethod.POST, "/api/deliveryRequest").hasRole("STOREFRONT") // SF can request delivery
                .antMatchers(HttpMethod.PUT, "/api/product/**").hasRole("STOREFRONT") // SF can request a booking
                .antMatchers(HttpMethod.PUT, "/api/order/**/received").hasRole("STOREFRONT") // SF can confirm a finished order

                .antMatchers("/api/deliveryRequests", "/api/deliveryRequest/**").hasRole("DELIVERY_DEPT")

                .antMatchers("/api/order/**/payed").hasRole("PAYMENT_SYSTEM") // Payment system can confirm we received money

                .anyRequest().authenticated()
            .and()
                .csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtSecret))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtSecret, users))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .httpBasic().disable();
    }
}