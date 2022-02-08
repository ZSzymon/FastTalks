package server.exceptions;

/*
Custom exception to easy debugging.
 */
public class DownloadMessagesException extends Exception {
    public DownloadMessagesException(String message, Throwable cause){
        super(message, cause);
    }
}