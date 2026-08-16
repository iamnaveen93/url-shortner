package in.practice.urlshortner.exception;

public class URLExpiredException extends RuntimeException {

    public URLExpiredException(String message) {
        super(message);
    }
}
