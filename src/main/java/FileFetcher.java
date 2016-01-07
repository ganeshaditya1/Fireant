import com.mongodb.MongoClient;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by aditya on 1/5/16.
 */
public class FileFetcher extends Thread {
    private Socket client;
    private String requestedResource;
    private MongoClient conn;
    private Bufferpool pool;

    public FileFetcher(Socket client, String requestedResource, MongoClient conn, Bufferpool pool){
        this.client = client;
        this.requestedResource = requestedResource;
        this.conn = conn;
        this.pool = pool;
    }

    public void run() {
        try {
            byte[] file = pool.getResource(requestedResource);
        }
        catch(IOException IOE){
            System.out.println("[Error]" + IOE.getMessage());
            return;
        }
    }
}
