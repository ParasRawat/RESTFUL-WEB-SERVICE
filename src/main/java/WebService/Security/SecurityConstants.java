package WebService.Security;

import WebService.SpringApplicationContext.SpringApplicationContext;
import org.springframework.boot.SpringApplication;

public class SecurityConstants {
    public static final long EXPIRATION_TIME=864000000;
    public static final String TOKEN_PREFIX="Bearer ";
    public static final String HEADER_STRING="Authorization";
    public static final String SIGN_UP_URL="/users";
    public static final String Verification_Email_Url="/users/email-verification";



    public static String getTokenSecret(){
        AppProperties appProperties=(AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }

}
