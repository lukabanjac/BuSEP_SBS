package rs.ac.uns.ftn.administratorappapi.dto;

public class MessageDTO {
    private boolean success;
    private String message;

    public MessageDTO(){
        super();
    }

    public MessageDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
