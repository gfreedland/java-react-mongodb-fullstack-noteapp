import java.util.Date;

public class ResponseBuilder {
    private Date date = null;
    private String responseCode = null;
    private NoteDTO[] response = null;
    private String _id = null;
    private String noteData = null;


    public void setDate(Date date){
        this.date = date;
    }
    public void setResponseCode(String responseCode){
        this.responseCode = responseCode;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setResponse(NoteDTO[] response) {
        this.response = response;
    }

    public void setStringResponse(String _id){
        this._id = _id;
    }

    public void setNoteData(String body) {
        this.noteData = body;
    }

    public ResponseDTO build(){
        ResponseDTO r = new ResponseDTO();
        r.setDate(date);
        r.setResponseCode(responseCode);
        r.set_id(_id);
        r.setResponse(response);
        return r;
    }
}
