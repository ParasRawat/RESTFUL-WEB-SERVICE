package WebService.Exceptions;


import WebService.Model.ErrorMessageModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {

    //EXCEPTION HANDLING IS SOMETHING, THAT DOES NOT STOP OUR PROGRAM FROM RUNNING AND AT THE SAME TIME CATCHES THE EXCEPTION AND DOES WHAT WE WANT TO DO WITH IT
    //WE CAN SPECIFY MULTIPLE EXCEPTIONS HERE SEPARATING BY COMMA
    @ExceptionHandler(value = {UserServiceException.class,NullPointerException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request){

        ErrorMessageModel errorMessageModel=new ErrorMessageModel(new Date(),ex.getMessage());
        return new ResponseEntity<>(errorMessageModel,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }



    //Handling unhandled exceptions
    //Now our program will not crash at any crash. At any crash our program wont crash
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request){

        ErrorMessageModel errorMessageModel=new ErrorMessageModel(new Date(),ex.getMessage());
        return new ResponseEntity<>(errorMessageModel,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
