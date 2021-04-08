package app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final UserDetailsProvider userDetails;

    @Bean
    public BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserDetailsProvider userDetailsProvider) {
        this.userDetails = userDetailsProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/products", "/api/product/**").permitAll() // Anyone can view products
                .antMatchers(HttpMethod.POST, "/api/order").permitAll() // Anyone can create a new order

                .antMatchers(HttpMethod.GET, "/api/orders", "/api/order/**").hasRole("STOREFRONT")
                .antMatchers(HttpMethod.POST, "/api/deliveryRequest").hasRole("STOREFRONT") // SF can request delivery
                .antMatchers(HttpMethod.PUT, "/api/product/**").hasRole("STOREFRONT") // SF can request a booking
                .antMatchers(HttpMethod.PUT, "/api/order/**/received").hasRole("STOREFRONT") // SF can confirm a finished order

                .antMatchers("/api/deliveryRequests", "/api/deliveryRequest/**").hasRole("DELIVERY_DEPT")

                .antMatchers("/api/order/**/payed").hasRole("PAYMENT_SYSTEM") // Payment system can confirm we received money

                .anyRequest().authenticated()
            .and()
                .csrf().disable()
                .formLogin()
                .loginProcessingUrl("/login").successForwardUrl("/ok").failureForwardUrl("/err")
            .and()
                .logout();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetails)
                .passwordEncoder(getEncoder());
    }
}