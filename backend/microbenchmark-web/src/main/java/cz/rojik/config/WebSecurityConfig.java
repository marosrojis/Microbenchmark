package cz.rojik.config;

import cz.rojik.backend.auth.StatelessAuthenticationFilter;
import cz.rojik.backend.auth.StatelessLoginFilter;
import cz.rojik.backend.auth.TokenAuthenticationService;
import cz.rojik.backend.auth.user.UserDetailService;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.backend.entity.RoleType;
import cz.rojik.error.MBenchmarkAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private StatelessAuthenticationFilter statelessAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // you USUALLY want this
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.eraseCredentials(true)
				.userDetailsService(userDetailService)
				.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(MappingURLConstants.LOGIN).permitAll()
				.antMatchers(MappingURLConstants.SOCKET + "/**").permitAll()
				.antMatchers(MappingURLConstants.TEST + "/**").hasRole(RoleType.USER.getRoleType())
                .antMatchers()
                    .hasRole(RoleType.Values.ROLE_USER_VALUE)
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers(MappingURLConstants.API_PREFIX + "/**").fullyAuthenticated()
                .antMatchers(MappingURLConstants.API_PREFIX + "/**").permitAll()
                .and()
			.exceptionHandling().accessDeniedHandler(new MBenchmarkAccessDeniedHandler())
			.and()
				// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
			.addFilterBefore(new StatelessLoginFilter(MappingURLConstants.LOGIN, tokenAuthenticationService, userDetailService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				// custom Token based authentication based on the header previously given to the client
			.addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
			.csrf().disable();
	}

}