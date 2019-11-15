package WebService.Model;

import java.util.Date;


//CUSTOM USER DEFINED ERROR MESSAGE
public class ErrorMessageModel {
    private Date timestamp;
    private String message;

    public ErrorMessageModel(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public ErrorMessageModel() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
