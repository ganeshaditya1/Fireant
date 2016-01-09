import com.mongodb.MongoClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 * Created by aditya on 1/5/16.
 */
public class FileFetcher extends Thread {
    private Socket client;
    private String requestedResource;
    private Bufferpool pool;

    public FileFetcher(Socket client, Request req, Bufferpool pool){
        this.client = client;
        this.requestedResource = req.getRequestURL();
        this.pool = pool;
    }

    public void run() {
        try {
            byte[] file = pool.getResource(requestedResource);
            try {
                PrintWriter out =
                        new PrintWriter(client.getOutputStream(), true);

                Response res = new Response(200, file, requestedResource);
                res.writeData(client);
                client.close();
            }
            catch(IOException ioe){
                System.out.println("[ERROR] "+ioe.getMessage());
            }
        }
        catch(IOException IOE){
            System.out.println("[Error]" + IOE.getMessage());
            try {
                PrintWriter out =
                        new PrintWriter(client.getOutputStream(), true);

                Response res = new Response(404, "<h1> Sorry we couldn't find what you were looking for</h1><hr>\r\n");
                res.writeData(client);
                client.close();
            }
            catch(IOException ioe){}

            return;
        }
    }
}
