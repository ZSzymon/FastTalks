package server.exceptions;

/*
Custom exception to easy debugging.
 */
public class LoginException extends Exception {
    public LoginException(String message, Throwable cause){
        super(message, cause);
    }
}