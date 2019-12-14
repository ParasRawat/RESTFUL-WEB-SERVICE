package WebService.Shared.dto;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

public class AmazonSES {

    public final String FROM="parasmyname@gmail.com";
    public final String SUBECT="LAST STEP TO COMPLETE REGISTRATION";

    public final String HTMLBODY="<h1> Please verfiy email</h1>"
            +"<p>Click on the link</p>"
            +"<a href ='http://localhost:8080/mywebservice/users/email-verification.html?token=$tokenval'>"
            +"<br/>"
            +"Thanking you";

    public final String TEXTBODY="Please verufy the email";

    BasicAWSCredentials basicAWSCredentials=new BasicAWSCredentials("AKIATDXGK64NXSWRK2N7","pH6oXtt0xPifwjEaWaEIiNE+UpJbaPOIYu9V9o0J");

    public void verifyEmail(UserDto userDto){

        AmazonSimpleEmailService amazonSimpleEmailService= AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(Regions.DEFAULT_REGION).build();

        String htmlBodyWithToken=HTMLBODY;
        String textBodyWithToken=TEXTBODY+userDto.getEmailVerificationToken();

        SendEmailRequest request=new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message()
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBECT)))
                        .withSource(FROM);

        amazonSimpleEmailService.sendEmail(request);

        System.out.println("Email Sent");



        //TO BE COMPLETED ON AFTER BUILDIG A REACTIVE CALL
    }
}
