
package com.luxosft.shapshot.jwt;


import com.luxosft.shapshot.service.UserDetailsServiceBasic;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
  private final UserDetailsServiceBasic userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final SecurityConstraints securityConstraints;
  public WebSecurity(UserDetailsServiceBasic userDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder, SecurityConstraints securityConstraints) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.securityConstraints = securityConstraints;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, securityConstraints.getSignUpUrl()).permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilter(new JWTAuthFilter(securityConstraints,authenticationManager()))
        .addFilter(new JWTAuthorizationFilter(securityConstraints,authenticationManager()))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }


}
