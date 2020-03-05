import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Set;

import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId; //not necessary
import spark.Request;
import spark.Response;

public class SparkDemo {
  public static DAO dao = new DAO();

  //not necessary method
  public static String processRoute(Request req, Response res) {
    Set<String> params = req.queryParams();
    for (String param : params) {
      // possible for query param to be an array
      System.out.println(param + " : " + req.queryParamsValues(param)[0]);
    }
    // do stuff with a mapped version http://javadoc.io/doc/com.sparkjava/spark-core/2.8.0
    // http://sparkjava.com/documentation#query-maps
    // print the id query value
    System.out.println(req.queryMap().get("id").value());
    return "done!";
  }

  public static void main(String[] args) {
    port(1234);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Adds note you make in Postman request body
    post("/store", (req, res) -> {
      res.type("application/json");
      res.header("Access-Control-Allow-Origin", "*");
      ResponseBuilder rb = new ResponseBuilder();
      rb.setDate(new Date());
      rb.setResponseCode("OK");
      dao.addNoteToDB(req.body());
      rb.setStringResponse(dao.documentIdToResponse());
      return gson.toJson(rb.build());
    });

    // Show Database as array of objects (notes)
    get("/list", (req, res) -> {
      res.header("Access-Control-Allow-Origin", "*");
      res.type("application/json");
      System.out.println("getting list");
      ResponseBuilder rb = new ResponseBuilder();
      rb.setDate(new Date());
      rb.setResponseCode("OK");
      rb.setResponse(dao.getAllNotes());
      res.body(gson.toJson(rb.build()));
      System.out.println(res.body());
      return gson.toJson(rb.build());
    });

    post("/delete", (req, res) -> {
      res.header("Access-Control-Allow-Origin", "*");
      res.type("application/json");
      ResponseBuilder rb = new ResponseBuilder();
      if(req.queryMap().get("_id").value() != null) {
        String id = req.queryMap().get("_id").value().trim();
        //System.out.println(id);
        if(dao.deleteNote(id) == true){
          rb.setStringResponse(dao.documentIdToResponse());
        }
      }
      rb.setDate(new Date());
      rb.setResponseCode("OK");
      return gson.toJson(rb.build());
    });

    //Get by specific id
    get("/get", (req, res) -> {
      res.header("Access-Control-Allow-Origin", "*");
      res.type("application/json");
      ResponseBuilder rb = new ResponseBuilder();
      if(req.queryMap().get("_id").value() != null) {
        String id = req.queryMap().get("_id").value().trim();
        System.out.println(id);
        if(dao.getNote(id) == true){
          rb.setStringResponse(dao.documentIdToResponse());
          rb.setResponse(dao.getOneNote(id));
        }
      }
      rb.setDate(new Date());
      rb.setResponseCode("OK");
      return gson.toJson(rb.build());
    });

    //update a post by id with request body
    post("update", (req, res) -> {
      res.header("Access-Control-Allow-Origin", "*");
      res.type("application/json");
      ResponseBuilder rb = new ResponseBuilder();
      System.out.println(req.body());
      if(req.queryMap().get("_id").value() != null) {
        String id = req.queryMap().get("_id").value().trim();
        if(dao.getNote(id) == true){
          rb.setStringResponse(dao.documentIdToResponse());
          dao.updateNote(id, req.body());
          rb.setResponse(dao.getAllNotes());
        }
      }
      rb.setDate(new Date());
      rb.setResponseCode("OK");
      return gson.toJson(rb.build());
    });

    // calling get will make your app start listening for the GET path with the /hello endpoint
    get("/hello", (req, res) -> "Hello World");
    get("/hey", (req, res) -> "Testing GET request");

    // showing a lambda expression with block body
    get("/test", (req, res) -> {
      // print some stuff about the request
      // http://sparkjava.com/documentation#routes
      System.out.println(req.attributes());
      System.out.println(req.headers());
      System.out.println(req.ip());
      System.out.println(req.url());
      System.out.println(req.userAgent());
      return "This one has a block body";
    });

    // Slightly more advanced routing
    path("/api", () -> {
      get("/users", (req, res) -> {
        return "This one has a block body";
      });
      get("/posts", SparkDemo::processRoute);
      get("/404test", (req, res) -> {
        // print some stuff about the request
        res.status(404);
        return "";
      });
    });
    // End of main
  }
}