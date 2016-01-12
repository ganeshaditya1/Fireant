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
import com.mongodb.MongoClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aditya on 1/6/16.
 *
 * This is the crux of the program.
 */
public class main {

    private Map<String, String> options =  new HashMap<String, String>();
    private void sopn(Object o){
        System.out.println(o);
    }

    private void sop(Object o){
        System.out.print(o);
    }

    private void printHelpMenu(){
        sopn("Usage: main [OPTIONS]");
        sopn("Options:");
        sopn("--portno \t The port number where the server needs to run");
        sopn("--mongoip \t The ip address where MongoDB is running");
        sopn("--mongoportno \t The port number where the mongo is running");
        sopn("--root \t The location from where I need to serve the requested files from");
        sopn("--maxcachesize \t The maximum number of files that should be cached");
    }

    private boolean isNumber(String input){
        try{
            Integer.parseInt(input);
        }
        catch(NumberFormatException NFE){
            return false;
        }
        return true;
    }


    public main(String arguments[]) throws IOException{
        if(arguments.length != 0 && arguments[0].equals("--help")){
            printHelpMenu();
            return;
        }
        else{
            for(int i = 0; i < arguments.length; i+=2){
                if(i < arguments.length && i+1 < arguments.length ){
                    options.put(arguments[i].substring(2).trim(), arguments[i + 1]);
                }
                else{
                    sopn("Missing argument");
                    return;
                }
            }
        }
        if(options.get("portno") == null || !isNumber(options.get("portno"))){
            sopn("[Warning]Either portno was not specified or was not a number, so using port no 80");
            options.put("portno", "80");
        }

        if(options.get("root") == null){
            sopn("[Warning]root was not specified");
            options.put("root", "/");
        }

        if(options.get("maxcachesize") == null || !isNumber(options.get("maxcachesize"))){
            sopn("[Warning]Either maxcachesize was not specified or was not a number. Using max cache size of 25");
            options.put("maxcachesize", "25");
        }

        if(options.get("mongoip") == null){
            sopn("[Warning] Ip address of mongo server was not specified so using localhost as server address");
            options.put("mongoip", "localhost");
        }

        if(options.get("mongoportno") == null || !isNumber(options.get("mongoportno"))){
            sopn("[Warning] Port no of mongo server was not specified so using 27017");
            options.put("mongoportno", "27017");
        }



        Bufferpool pool = new Bufferpool(Integer.parseInt(options.get("maxcachesize")), options.get("root"));
        MongoClient mongoConn = new MongoClient(options.get("mongoip"), Integer.parseInt(options.get("mongoportno")));

        ServerSocket s = new ServerSocket(Integer.parseInt(options.get("portno")));
        while(true){
            Socket client = s.accept();
            Request req = new Request(client);
            if(req.getMethod().equals("GET")){
                (new FileFetcher(client, req, pool)).run();
            }
            else if(req.getMethod().equals("POST")){
                try {
                    (new MongoHandler(client, req, mongoConn)).run();
                }
                catch(Exception e){
                    Response res = new Response(400, "{\"Error\": \""+e.getMessage()+"\"}");
                    sopn("[Error]" + e.getMessage());
                    res.writeData(client);
                    client.close();
                }
            }

        }

    }

    public static void main(String arguments[]) throws IOException{
        new main(arguments);
    }
}
