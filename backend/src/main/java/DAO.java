import com.google.gson.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class DAO {
    // connect to mongodb Database,
    // Database is made locally by running Robo3T or MongoDB Compass Community
    static MongoClient client = new MongoClient("localhost", 27017);
    static MongoDatabase myDatabase = client.getDatabase("myDatabase");
    static MongoCollection<Document> myCollection = myDatabase.getCollection("myCollection");
    static Document document = null;

    public static void addNoteToDB(String body) {
        System.out.println("added note " + body);
        document = new Document("data", body);
        document.append("date", new Date().toString());
        document.append("_id", new ObjectId().toString());
        myCollection.insertOne(document);
    }

    public String documentIdToResponse() {
        Document temp = new Document();
        temp = document;
        document = null;
        // System.out.println("trying to get id");
        //return temp.getObjectId("_id").toString();
        if(temp != null) {
            return temp.getString("_id");
        } else {
            return null;
        }
    }

    public NoteDTO[] getAllNotes() {
        NoteDTO[] notes = null;
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject obj = new JsonObject();
        String myColl = "";
        MongoCursor<Document> cursor = myCollection.find().iterator();
        try {
            int counter = 0;
            myColl+="{ \"notes\" : [ ";
            while (cursor.hasNext()) {
                counter++;
                Document a = new Document();
                a = cursor.next();
                // a.append("id", a.getObjectId("_id").toString());
                myColl += a.toJson().trim();
                if (counter < myCollection.count()){
                    myColl += ",";
                }
            }
            myColl+="]}";
        } finally {
            cursor.close();
        }
        //Make sure myColl is correct JSON syntax
        try{
            obj = jsonParser.parse(myColl).getAsJsonObject();
            notes = gson.fromJson(obj.get("notes"), NoteDTO[].class);
        } catch (Exception e){
            System.out.println(e);
            notes = null;
        }
        return notes;
    }

    public boolean deleteNote(String id) {
        Document a = new Document();
        System.out.println(id);
        try {
            a = myCollection.find(eq("_id", id)).first();
            System.out.println("found : " + a.toJson());
            document = a;
            myCollection.deleteOne(a);
            return true;
        } catch(Error e) {
            System.out.println("not found");
            document = null;
            return false;
        }
    }

    public boolean getNote(String id) {
        Document a = new Document();
        System.out.println(id);
        try {
            a = myCollection.find(eq("_id", id)).first();
            System.out.println("found : " + a.toJson());
            document = a;

            return true;
        } catch(Error e) {
            System.out.println("not found");
            document = null;
            return false;
        }
    }

    public NoteDTO[] getOneNote(String id) {
        Document a = myCollection.find(eq("_id", id)).first();
        NoteDTO[] note = null;
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject obj = new JsonObject();
        String myColl = "{ \"notes\" : [ " + a.toJson() + "]}";
        obj = jsonParser.parse(myColl).getAsJsonObject();
        note = gson.fromJson(obj.get("notes"), NoteDTO[].class);
        return note;
    }

    public void updateNote(String id, String body) {
        Document a = myCollection.find(eq("_id", id)).first();
        Document temp = a;
        myCollection.deleteOne(a);
        temp.replace("data", body );
        myCollection.insertOne(temp);
        System.out.println("updated");
    }
}
