package rs.ac.uns.ftn.administratorappapi.exception;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


public class ParentCertificateExpireException extends RuntimeException {
    public ParentCertificateExpireException() {
        super(ParentCertificateExpireException.generateMessage());
    }

    private static String generateMessage() {
        return "Extend parent expiration date.";
    }

}