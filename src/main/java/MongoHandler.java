/*
Copyright (c) 2016, Aditya Ambadipudi
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of SilkRoad nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.mongodb.util.ObjectSerializer;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.bson.Document;
import org.jongo.Find;
import org.jongo.Jongo;
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
        System.out.println(req.getContent());
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

    private void selectOne(){

        Jongo jongo = new Jongo(dbconn.getDB(database));
        org.jongo.MongoCollection mc = jongo.getCollection(collection);
        JSONObject findField = (JSONObject)data.get("findField");
        JSONObject sortField = (JSONObject)data.get("sortField");
        JSONObject projectionField = (JSONObject)data.get("projectionField");

        Find result = null;
        if(findField != null){
            result = mc.find(findField.toJSONString());
        }
        if(sortField != null){
            result = result.sort(sortField.toString());
        }
        if(projectionField != null){
            result = result.projection(projectionField.toString());
        }
        String resultJson = "";
        org.jongo.MongoCursor cursor = result.as(Object.class);
        if(cursor.hasNext()){
            resultJson += cursor.next();
        }
        System.out.println(resultJson);
        Response res= new Response(200, resultJson);
        try {
            res.writeData(client);
        }
        catch(IOException ioe){
            System.out.println("[Error]" + ioe.getMessage());
        }
    }

    private void selectMany(){
        Jongo jongo = new Jongo(dbconn.getDB(database));
        org.jongo.MongoCollection mc = jongo.getCollection(collection);
        JSONObject findField = (JSONObject)data.get("findField");
        JSONObject sortField = (JSONObject)data.get("sortField");
        JSONObject projectionField = (JSONObject)data.get("projectionField");

        Find result = null;
        if(findField != null){
            result = mc.find(findField.toJSONString());
        }
        if(sortField != null){
            result = result.sort(sortField.toString());
        }
        if(projectionField != null){
            result = result.projection(projectionField.toString());
        }
        String resultJson = "";
        org.jongo.MongoCursor cursor = result.as(Object.class);
        while(cursor.hasNext()){
            resultJson += cursor.next();
        }
        System.out.println(resultJson);
        Response res= new Response(200, resultJson);
        try {
            res.writeData(client);
        }
        catch(IOException ioe){
            System.out.println("[Error]" + ioe.getMessage());
        }
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
        else if(operation.equals("select one")){
            selectOne();
        }
        else if(operation.equals("select many")){
            selectMany();
        }
        try {
            client.close();
        }
        catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }
}
