import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aditya on 1/9/16.
 */
public class Request {

    private Map<String, String> headerFields = new HashMap<String, String>();
    private String protocol, request, method, content;

    public Request(Socket client) throws IOException{
        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));

        String statusLine = in.readLine();
        String keywords[] = statusLine.split(" ");
        method = keywords[0];
        request = keywords[1];
        protocol = keywords[2];

        String line = "";
        byte state = 0;
        char alphabet;
        while(true){
            alphabet = (char)in.read();
            if(state == 0){
                if(alphabet == '\r') {
                    state = 1;
                }
                else{
                    line += alphabet;
                }
            }
            else if(state == 1){
                if(alphabet == '\n'){
                    state = 2;
                    String arr[] = line.split(":");
                    headerFields.put(arr[0], arr[1]);
                    line = "";
                }
                else{
                    line += alphabet;
                }
            }
            else if(state == 2){
                if(alphabet == '\r'){
                    in.read();
                    break;
                }
                else{
                    line += alphabet;
                    state = 0;
                }
            }
        }

        if(headerFields.get("Content-Length") != null) {
            int messageLength = Integer.parseInt(headerFields.get("Content-Length").trim());
            content = "";
            for (int i = 0; i < messageLength; i++) {
                content += (char) in.read();
            }
        }
    }

    public Map getHeaderFields(){
        return headerFields;
    }

    public String getRequestURL(){
        return request;
    }

    public String getMethod(){
        return method;
    }

    public String getContent(){
        return content;
    }

    public String getProtocol(){
        return protocol;
    }

}
