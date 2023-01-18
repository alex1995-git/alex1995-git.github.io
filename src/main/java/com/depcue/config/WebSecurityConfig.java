package com.depcue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // We don't need CSRF for this example
//		http.csrf().disable()
//				.authorizeRequests()
//				// dont authenticate this particular request
//				.antMatchers("/authenticate").permitAll()
//				// all other requests need to be authenticated
//				.antMatchers("/authenticate").permitAll()
//				.antMatchers("/usuarios").denyAll()
//				.antMatchers("/usuarios/**").denyAll()
//				.antMatchers(HttpMethod.GET, "/registrosabonos///").hasRole("USER")
//				.antMatchers(HttpMethod.POST, "/registrosabonos///").hasRole("USER")
//				.antMatchers(HttpMethod.PUT, "/registrosabonos///").hasRole("USER")
//				.antMatchers(HttpMethod.DELETE, "/registrosabonos///").hasRole("USER")
//				.antMatchers(HttpMethod.HEAD, "/registrosabonos///").hasRole("USER")
//				.antMatchers(HttpMethod.OPTIONS, "/registrosabonos///").hasRole("USER")
//				.antMatchers(HttpMethod.TRACE, "/registrosabonos///").hasRole("USER")
//
//				.and()
//				// make sure we use stateless session; session won't be used to
//				// store user's state.
//				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//				.and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				;
//
//		// Add a filter to validate the tokens with every request
//		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


        http.csrf().disable()
                .authorizeRequests()
                // dont authenticate this particular request
                .antMatchers("/authenticate").permitAll()
                //.antMatchers("/authenticateValidate").permitAll()
                .and()
                .requestMatchers(matchers -> matchers
                        .antMatchers("/registrosabonos/**")
                        .antMatchers("/abonos/**")
                        .antMatchers("/cargaMasiva/**")
                        .antMatchers("/authenticate")
                        .antMatchers("/authenticateValidate")
                )
                .authorizeRequests(authz -> authz
                        // .anyRequest().authenticated() /* solicita autenticacion para accesos */
                        .anyRequest().permitAll() /* permite a todos acceder a todas las rutas o apis */
                )
                //.requiresChannel(channel -> channel.anyRequest().requiresSecure() /* habilita certificacion ssl y https */
                //)
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        ;
//        .authorizeRequests(authorize ->authorize.anyRequest().permitAll())
//        .build();
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.csrf().disable().authorizeRequests().antMatchers("/authenticate").permitAll().
//                anyRequest().authenticated().and().
//                exceptionHandling().
//                authenticationEntryPoint(jwtAuthenticationEntryPoint).
//                and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//    }
}