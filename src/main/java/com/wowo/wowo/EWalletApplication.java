package com.wowo.wowo;

import com.wowo.wowo.security.filter.ApiServiceFilter;
import com.wowo.wowo.security.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity
@EnableJpaRepositories
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
@EnableKafka
public class EWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(EWalletApplication.class, args);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            ApiServiceFilter apiServiceFilter) throws
                                               Exception {
        http.formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


       /* http.authorizeHttpRequests(auth -> auth.requestMatchers("/v?/auth/**",
                        "/api/v?/partner/register","swagger-ui/**","docs/**", "v3/api-docs/**"
                )
                .permitAll()
                .requestMatchers("/v?/user/**")
                .hasRole("USER")
                .requestMatchers("/v?/partner/**",
                        "/v?/payment/**")
                .hasRole("PARTNER")
                .requestMatchers("/v?/admin/**")
                .hasRole("ADMIN")
                .anyRequest()
                .fullyAuthenticated());


        //add token filter to security filter chain
        http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(apiServiceFilter, TokenFilter.class);
*/
//        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
//                authorizationManagerRequestMatcherRegistry.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOriginPattern("https://*:[*]");
        corsConfiguration.addAllowedOriginPattern("http://*:[*]");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}
