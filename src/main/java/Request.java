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
