package WebService.Model;


//enum are basically named constants
public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("PLEASE GO THROUGH THE DOCUMENTATION"),
    RECORD_ALREADY_EXIST("AS IT SAYS, RECORD ALREADY EXISTS"),
    INTERNAL_SERVER_ERROR("ACTUALLY INTERNAL SERVER ERROR"),
    AUTHENTICATION_FAILED("AUTHENTICATION HAS FAILED, YOU ARE NOT A VALID USER"),
    COULD_NOT_UPDATE_THE_RECORD("COULD NOT UPDATE THE RECORD"),
    EMAIL_ADDRESS_NOT_VERIFIED("EMAIL ADDRESS COULD NOT BE VERIFIED, SORRY, NOT AUTHENTICATED,");


    private String errorMessage;

    ErrorMessages(String errorMessage){
        this.errorMessage=errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
