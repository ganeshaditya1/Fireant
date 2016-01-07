import com.mongodb.MongoClient;

import java.io.IOException;
import java.io.PrintWriter;
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
            try {
                PrintWriter out =
                        new PrintWriter(client.getOutputStream(), true);

                Response res = new Response(200);
                res.addContentType(requestedResource);
                res.addHeaderEntry("Content-Length", file.length + "");
                out.write(res.getResponse());
                out.flush();
                client.getOutputStream().write(file);
                out.close();
            }
            catch(IOException ioe){}
        }
        catch(IOException IOE){
            System.out.println("[Error]" + IOE.getMessage());
            try {
                PrintWriter out =
                        new PrintWriter(client.getOutputStream(), true);

                Response res = new Response(404);
                res.addHeaderEntry("Content-Type", "text/html");
                String message = "<h1> Sorry we couldn't find what you were looking for</h1><hr>\r\n";
                res.addHeaderEntry("Content-Length", message.length() + "");
                out.write(res.getResponse());
                out.write(message);
                out.flush();
                out.close();
            }
            catch(IOException ioe){}
            return;
        }
    }
}
