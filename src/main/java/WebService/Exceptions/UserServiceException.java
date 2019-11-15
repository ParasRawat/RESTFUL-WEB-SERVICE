package WebService.Exceptions;

public class UserServiceException extends RuntimeException {

    public static final long serialVersionUID=123456765345l;

    public UserServiceException(String message) {
        super(message);

    }

}
