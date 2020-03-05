import org.bson.types.ObjectId; //Not necessary

import java.util.Date;

public class NoteDTO {
    private String data = null;
    //private ObjectId _id = null;
    private String _id = null;
    private String date = null;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date.toString();
    }
/*
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
*/
    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
