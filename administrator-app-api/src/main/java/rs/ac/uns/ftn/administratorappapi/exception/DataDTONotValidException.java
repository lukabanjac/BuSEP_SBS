package rs.ac.uns.ftn.administratorappapi.exception;


public class DataDTONotValidException
        extends Exception {
    public DataDTONotValidException(String errorMessage) {
        super(errorMessage);
    }
}