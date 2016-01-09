import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by aditya on 1/8/16.
 */
public class MongoHandler extends Thread {
    private String database, collection, operation;
    private JSONObject data;
    private MongoClient dbconn;
    private MongoDatabase mdb;
    private MongoCollection<Document> mcollection;
    private Socket client;

    public MongoHandler(Socket client, Request req, MongoClient mongoconn) throws Exception {
        Object obj = JSONValue.parse(req.getContent());
        JSONObject jobj = (JSONObject)obj;
        System.out.println(JSONValue.parse(req.getContent()) + " a " + req.getContent());
        if((database = (String)jobj.get("database")) == null) {
            throw new Exception("Database not specified");
        }
        if((collection = (String)jobj.get("collection")) == null){
            throw new Exception("Collection not specified");
        }
        if((operation = (String)jobj.get("operation")) == null){
            throw new Exception("Operation not specified");
        }
        data = (JSONObject)jobj.get("data");
        this.dbconn = mongoconn;

        mdb = dbconn.getDatabase(database);
        mcollection = mdb.getCollection(collection);
        this.client = client;
    }

    private void insert(){
        Document doc = Document.parse(data.toString());
        mcollection.insertOne(doc);
    }

    private void deleteOne(){
        Document doc = Document.parse(data.toString());
        mcollection.deleteOne(doc);
    }

    private void deleteMany(){
        Document doc = Document.parse(data.toString());
        mcollection.deleteMany(doc);
    }

    private void updateOne(){
        JSONObject selectionField = (JSONObject)data.get("selectionField");
        JSONObject replacement = (JSONObject)data.get("replacement");
        mcollection.updateOne(Document.parse(selectionField.toString()), Document.parse(replacement.toString()));
    }

    private void updateMany(){
        JSONObject selectionField = (JSONObject)data.get("selectionField");
        JSONObject replacement = (JSONObject)data.get("replacement");
        mcollection.updateMany(Document.parse(selectionField.toString()), Document.parse(replacement.toString()));
    }

    public void run(){
        if(operation.equals("insert")){
            insert();
        }
        else if(operation.equals("delete one")){
            deleteOne();
        }
        else if(operation.equals("delete many")){
            deleteMany();
        }
        else if(operation.equals("update one")){
            updateOne();
        }
        else if(operation.equals("update many")){
            updateMany();
        }
        try {
            client.close();
        }
        catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }
}
