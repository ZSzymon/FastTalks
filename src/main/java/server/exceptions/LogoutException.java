package server.exceptions;

/*
Custom exception to easy debugging.
 */
public class LogoutException extends Exception {
    public LogoutException(String message, Throwable cause){
        super(message, cause);
    }
}