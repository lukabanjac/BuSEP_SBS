package rs.ac.uns.ftn.administratorappapi.exception;

public class PkiMalfunctionException extends RuntimeException {
    public PkiMalfunctionException() {
        super("Malfunction in PKI system.");
    }

    public PkiMalfunctionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PkiMalfunctionException(final String message) {
        super(message);
    }

    public PkiMalfunctionException(final Throwable cause) {
        super(cause);
    }

}
