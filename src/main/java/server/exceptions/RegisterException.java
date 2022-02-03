package server.exceptions;

/*
Custom exception to easy debugging.
 */
public class RegisterException extends Exception {
    public RegisterException(String message, Throwable cause){
        super(message, cause);
    }
}
