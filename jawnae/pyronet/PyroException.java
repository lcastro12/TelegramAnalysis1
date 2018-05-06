package jawnae.pyronet;

public class PyroException extends RuntimeException {
    public PyroException(String msg) {
        super(msg);
    }

    public PyroException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
