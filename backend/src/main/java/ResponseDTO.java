import java.util.Date;

public class ResponseDTO {
    private Date date = null;
    private String responseCode = null;
    private String _id = null;
    private NoteDTO[] response = null;

    public Date getDate() {
        return date;
    }

    public String get_id() {
        return _id;
    }

    public NoteDTO[] getResponse() {
        return response;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setResponse(NoteDTO[] response) {
        this.response = response;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
