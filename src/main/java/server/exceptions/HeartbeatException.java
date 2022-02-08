package server.exceptions;


/*
Custom exception to easy debugging.
 */
public class HeartbeatException extends Exception{
    public HeartbeatException(String message, Throwable cause){
        super(message, cause);
    }
}
