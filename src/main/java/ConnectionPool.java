import com.mongodb.MongoClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya on 1/5/16.
 * '
 * Stores opened connection objects. This is to prevent opening fresh connections everytime there is a request.
 */
public class ConnectionPool {
    private List<MongoClient> pool = new ArrayList<MongoClient>();
    private int maxSize;
    private String hostIP;
    private int portNo;

    public ConnectionPool(int maxSize, String hostIP, int portNo){
        this.maxSize = maxSize;
        this.hostIP = hostIP;
        this.portNo = portNo;
    }

    public MongoClient getConnection(){
        return null;
    }

}
