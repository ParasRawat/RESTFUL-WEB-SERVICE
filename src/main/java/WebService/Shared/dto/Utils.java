package WebService.Shared.dto;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM=new SecureRandom();
    private final String Alphabet="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generatedUserId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){
        StringBuilder returnvalue=new StringBuilder(length);

        for(int i=0;i<length;i++){
            returnvalue.append(Alphabet.charAt(RANDOM.nextInt(Alphabet.length())));
        }

        return new String(returnvalue);
    }
}
