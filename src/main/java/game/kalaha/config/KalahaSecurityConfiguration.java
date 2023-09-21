//package game.kalaha.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class KalahaSecurityConfiguration {
//    /**
//     * This is when you want to add authorization and bearer tokens for your endpoints
//     * @param http
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeRequests()
//                .antMatchers(publicEndpoints()).permitAll().and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        return http.build();
//    }
//
//    private String[] publicEndpoints() {
//        return new String[]{"health/**"};
//    }
//}
