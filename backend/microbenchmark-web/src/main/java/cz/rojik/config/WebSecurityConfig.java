package cz.rojik.config;

import cz.rojik.backend.auth.StatelessAuthenticationFilter;
import cz.rojik.backend.auth.StatelessLoginFilter;
import cz.rojik.backend.auth.TokenAuthenticationService;
import cz.rojik.backend.auth.user.UserDetailService;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.backend.enums.RoleTypeEnum;
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

import javax.servlet.http.HttpServletResponse;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
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
				.antMatchers(MappingURLConstants.SOCKET + "/**").not().hasRole(RoleTypeEnum.DEMO.getRoleType())

				// BENCHMARK
				.mvcMatchers(HttpMethod.GET, MappingURLConstants.BENCHMARK).hasAnyRole(RoleTypeEnum.USER.getRoleType(), RoleTypeEnum.DEMO.getRoleType())
				.mvcMatchers(HttpMethod.GET, MappingURLConstants.BENCHMARK + "/" + MappingURLConstants.ID_PARAM).hasAnyRole(RoleTypeEnum.USER.getRoleType(), RoleTypeEnum.DEMO.getRoleType())
				.mvcMatchers(HttpMethod.DELETE, MappingURLConstants.BENCHMARK + "/" + MappingURLConstants.ID_PARAM).hasRole(RoleTypeEnum.USER.getRoleType())
				.mvcMatchers(HttpMethod.POST, MappingURLConstants.BENCHMARK + "/" + MappingURLConstants.BENCHMARK_ASSIGN_TO_USER).hasRole(RoleTypeEnum.ADMIN.getRoleType())

				// BENCHMARK_STATE
				.mvcMatchers(MappingURLConstants.BENCHMARK_STATE).hasRole(RoleTypeEnum.ADMIN.getRoleType())

				// LIBRARY
				.mvcMatchers(MappingURLConstants.LIBRARY_LIBRARIES).hasRole(RoleTypeEnum.ADMIN.getRoleType())

				// PROPERTY
				.mvcMatchers(MappingURLConstants.PROPERTY).hasRole(RoleTypeEnum.ADMIN.getRoleType())
				.mvcMatchers(MappingURLConstants.PROPERTY + "/**").hasRole(RoleTypeEnum.ADMIN.getRoleType())

				// PROJECT
				.mvcMatchers(HttpMethod.POST, MappingURLConstants.PROJECT + "/" + MappingURLConstants.PROJECT_CREATE).not().hasRole(RoleTypeEnum.DEMO.getRoleType())
				.mvcMatchers(HttpMethod.POST, MappingURLConstants.PROJECT + "/" + MappingURLConstants.PROJECT_IMPORT_LIBRARIES).not().hasRole(RoleTypeEnum.DEMO.getRoleType())
				.mvcMatchers(HttpMethod.POST, MappingURLConstants.PROJECT + "/" + MappingURLConstants.PROJECT_COMPILE).not().hasRole(RoleTypeEnum.DEMO.getRoleType())
				.mvcMatchers(HttpMethod.POST, MappingURLConstants.PROJECT + "/" + MappingURLConstants.PROJECT_KILL).hasRole(RoleTypeEnum.ADMIN.getRoleType())

				// USERS
				.mvcMatchers(HttpMethod.GET, MappingURLConstants.USERS).hasRole(RoleTypeEnum.ADMIN.getRoleType())
				.mvcMatchers(HttpMethod.GET, MappingURLConstants.USERS + "/" + MappingURLConstants.ID_PARAM).hasAnyRole(RoleTypeEnum.USER.getRoleType(), RoleTypeEnum.DEMO.getRoleType())
				.mvcMatchers(HttpMethod.PUT, MappingURLConstants.USERS + "/" + MappingURLConstants.ID_PARAM).hasAnyRole(RoleTypeEnum.USER.getRoleType(), RoleTypeEnum.DEMO.getRoleType())
				.mvcMatchers(HttpMethod.DELETE, MappingURLConstants.USERS + "/" + MappingURLConstants.ID_PARAM).hasRole(RoleTypeEnum.ADMIN.getRoleType())

				.antMatchers("/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(MappingURLConstants.API_PREFIX + "/**").fullyAuthenticated()
                .antMatchers(MappingURLConstants.API_PREFIX + "/**").permitAll()
                .and()
			.exceptionHandling()
				.accessDeniedHandler(new MBenchmarkAccessDeniedHandler())
				.authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
			.and()
				// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
			.addFilterBefore(new StatelessLoginFilter(MappingURLConstants.LOGIN, tokenAuthenticationService, userDetailService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				// custom Token based authentication based on the header previously given to the client
			.addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
			.csrf().disable();
	}

}