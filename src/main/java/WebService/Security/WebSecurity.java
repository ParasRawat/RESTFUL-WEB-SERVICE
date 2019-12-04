package WebService.Security;


import WebService.Service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;


@EnableWebSecurity

public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
     http.csrf().disable().authorizeRequests()
             .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
             .permitAll()
             .antMatchers(HttpMethod.GET, SecurityConstants.Verification_Email_Url)
             .permitAll()
             .anyRequest()
             .authenticated().and().addFilter(getAuthentication())
             .addFilter(new AuthorizationFilter(authenticationManager()))
             .sessionManagement()
             .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    private AthenticationFilter getAuthentication() throws Exception{
        final AthenticationFilter athenticationFilter=new AthenticationFilter(authenticationManager());
        athenticationFilter.setFilterProcessesUrl("/users/login");
        return athenticationFilter;
    }

}
