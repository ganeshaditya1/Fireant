/**
 * Created by aditya on 1/5/16.
 *
 * This class exposes methods that lets you assemble a response object.
 */
public class Response {

    private String result;
    public Response(int status){
    }

    public void addContentType(String fileName){}

    public void addHeaderEntry(String key, String value){}

    public String getResponse(){return "";}
}
