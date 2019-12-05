package WebService.Shared.dto;

import WebService.Security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM=new SecureRandom();
    private final String Alphabet="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static boolean hasTokenExpired(String token) {

        Claims claims= Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret())
                .parseClaimsJws(token).getBody();

        Date tokenEpirationDate=claims.getExpiration();
        Date today=new Date();

        return tokenEpirationDate.before(today);



    }

    public String generatedUserId(int length){
        return generateRandomString(length);
    }
    public String generatedAddressId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){
        StringBuilder returnvalue=new StringBuilder(length);

        for(int i=0;i<length;i++){
            returnvalue.append(Alphabet.charAt(RANDOM.nextInt(Alphabet.length())));
        }

        return new String(returnvalue);
    }

    //GENERATING EMAIL VERIFICATION TOKEN
    public String generateEmailVerificationToken(String publicuserId) {

        String token=Jwts.builder()
                .setSubject(publicuserId)
                .setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.getTokenSecret())
                .compact();

        return token;


    }
}
