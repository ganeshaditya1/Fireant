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
