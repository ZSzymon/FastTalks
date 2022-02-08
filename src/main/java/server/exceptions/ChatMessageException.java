package server.exceptions;

/*
Custom exception to easy debugging.
 */
public class ChatMessageException extends Exception {
    public ChatMessageException(String message, Throwable cause){
        super(message, cause);
    }
}