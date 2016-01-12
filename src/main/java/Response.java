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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aditya on 1/5/16.
 *
 * This class exposes methods that lets you assemble a response object.
 */
public class Response {

    private String headers;
    private Map<String, String> mimes = new HashMap<String, String>();
    private Boolean isFile;
    private byte[] file;
    private String url;
    private String message;

    private void addContentType(String fileName){
        int index = fileName.lastIndexOf('.');
        String extension = fileName.substring(index);
        addHeaderEntry("Content-Type: ", mimes.get(extension));
    }

    private void addHeaderEntry(String key, String value){
        headers += key + ": "  + value + " \r\n";
    }

    public Response(int status, byte[] file, String url){
        headers = "HTTP/1.1";
        if(status == 200){
            headers += " 200 OK";
        }
        else if(status == 404){
            headers += " 404 Not Found";
        }
        else if(status == 400){
            headers += " 400 Bad Request";
        }
        headers += "\r\n";
        addMimes();
        isFile = true;
        this.file = file;
        this.url = url;
        addContentType(url);
        addHeaderEntry("Content-Length", file.length + "");
        String temp = url.replace('\\', '/');
        String filename = temp.substring(temp.lastIndexOf("/"));
        //addHeaderEntry("Content-Disposition", "attachment; filename=" + filename);
        addHeaderEntry("Access-Control-Allow-Origin", "*");
    }

    public Response(int status, String message){
        headers = "HTTP/1.1";
        if(status == 200){
            headers += " 200 OK";
        }
        else if(status == 404){
            headers += " 404 Not Found";
        }
        else if(status == 400){
            headers += " 400 Bad Request";
        }
        headers += "\r\n";
        addMimes();
        isFile = false;
        this.message = message;
        if(message.indexOf('{') == -1)
        {
            addHeaderEntry("Content-Type", "text/html");
        }
        else {
            addHeaderEntry("Content-Type", "text");
        }
        addHeaderEntry("Content-length", message.length() + "");
        addHeaderEntry("Access-Control-Allow-Origin", "*");
    }



    public void writeData(Socket client) throws IOException {
        headers += "\r\n";
        if(isFile){
            PrintWriter out =
                    new PrintWriter(client.getOutputStream(), true);
            out.write(headers);
            out.flush();
            client.getOutputStream().write(file);
            out.close();
        }
        else{
            PrintWriter out =
                    new PrintWriter(client.getOutputStream(), true);
            out.write(headers + message);
            out.flush();
            out.close();
            System.out.println(headers + message);
        }
    }

    private void addMimes(){
        mimes.put(".3dm", "x-world/x-3dmf");
        mimes.put(".3dmf", "x-world/x-3dmf");
        mimes.put(".a", "application/octet-stream");
        mimes.put(".aab", "application/x-authorware-bin");
        mimes.put(".aam", "application/x-authorware-map");
        mimes.put(".aas", "application/x-authorware-seg");
        mimes.put(".abc", "text/vnd.abc");
        mimes.put(".acgi", "text/html");
        mimes.put(".afl", "video/animaflex");
        mimes.put(".ai", "application/postscript");
        mimes.put(".aif", "audio/aiff");
        mimes.put(".aif", "audio/x-aiff");
        mimes.put(".aifc", "audio/aiff");
        mimes.put(".aifc", "audio/x-aiff");
        mimes.put(".aiff", "audio/aiff");
        mimes.put(".aiff", "audio/x-aiff");
        mimes.put(".aim", "application/x-aim");
        mimes.put(".aip", "text/x-audiosoft-intra");
        mimes.put(".ani", "application/x-navi-animation");
        mimes.put(".aos", "application/x-nokia-9000-communicator-add-on-software");
        mimes.put(".aps", "application/mime");
        mimes.put(".arc", "application/octet-stream");
        mimes.put(".arj", "application/arj");
        mimes.put(".arj", "application/octet-stream");
        mimes.put(".art", "image/x-jg");
        mimes.put(".asf", "video/x-ms-asf");
        mimes.put(".asm", "text/x-asm");
        mimes.put(".asp", "text/asp");
        mimes.put(".asx", "application/x-mplayer2");
        mimes.put(".asx", "video/x-ms-asf");
        mimes.put(".asx", "video/x-ms-asf-plugin");
        mimes.put(".au", "audio/basic");
        mimes.put(".au", "audio/x-au");
        mimes.put(".avi", "application/x-troff-msvideo");
        mimes.put(".avi", "video/avi");
        mimes.put(".avi", "video/msvideo");
        mimes.put(".avi", "video/x-msvideo");
        mimes.put(".avs", "video/avs-video");
        mimes.put(".bcpio", "application/x-bcpio");
        mimes.put(".bin", "application/mac-binary");
        mimes.put(".bin", "application/macbinary");
        mimes.put(".bin", "application/octet-stream");
        mimes.put(".bin", "application/x-binary");
        mimes.put(".bin", "application/x-macbinary");
        mimes.put(".bm", "image/bmp");
        mimes.put(".bmp", "image/bmp");
        mimes.put(".bmp", "image/x-windows-bmp");
        mimes.put(".boo", "application/book");
        mimes.put(".book", "application/book");
        mimes.put(".boz", "application/x-bzip2");
        mimes.put(".bsh", "application/x-bsh");
        mimes.put(".bz", "application/x-bzip");
        mimes.put(".bz2", "application/x-bzip2");
        mimes.put(".c", "text/plain");
        mimes.put(".c", "text/x-c");
        mimes.put(".c++", "text/plain");
        mimes.put(".cat", "application/vnd.ms-pki.seccat");
        mimes.put(".cc", "text/plain");
        mimes.put(".cc", "text/x-c");
        mimes.put(".ccad", "application/clariscad");
        mimes.put(".cco", "application/x-cocoa");
        mimes.put(".cdf", "application/cdf");
        mimes.put(".cdf", "application/x-cdf");
        mimes.put(".cdf", "application/x-netcdf");
        mimes.put(".cer", "application/pkix-cert");
        mimes.put(".cer", "application/x-x509-ca-cert");
        mimes.put(".cha", "application/x-chat");
        mimes.put(".chat", "application/x-chat");
        mimes.put(".class", "application/java");
        mimes.put(".class", "application/java-byte-code");
        mimes.put(".class", "application/x-java-class");
        mimes.put(".com", "application/octet-stream");
        mimes.put(".com", "text/plain");
        mimes.put(".conf", "text/plain");
        mimes.put(".cpio", "application/x-cpio");
        mimes.put(".cpp", "text/x-c");
        mimes.put(".cpt", "application/mac-compactpro");
        mimes.put(".cpt", "application/x-compactpro");
        mimes.put(".cpt", "application/x-cpt");
        mimes.put(".crl", "application/pkcs-crl");
        mimes.put(".crl", "application/pkix-crl");
        mimes.put(".crt", "application/pkix-cert");
        mimes.put(".crt", "application/x-x509-ca-cert");
        mimes.put(".crt", "application/x-x509-user-cert");
        mimes.put(".csh", "application/x-csh");
        mimes.put(".csh", "text/x-script.csh");
        mimes.put(".css", "application/x-pointplus");
        mimes.put(".css", "text/css");
        mimes.put(".cxx", "text/plain");
        mimes.put(".dcr", "application/x-director");
        mimes.put(".deepv", "application/x-deepv");
        mimes.put(".def", "text/plain");
        mimes.put(".der", "application/x-x509-ca-cert");
        mimes.put(".dif", "video/x-dv");
        mimes.put(".dir", "application/x-director");
        mimes.put(".dl", "video/dl");
        mimes.put(".dl", "video/x-dl");
        mimes.put(".doc", "application/msword");
        mimes.put(".dot", "application/msword");
        mimes.put(".dp", "application/commonground");
        mimes.put(".drw", "application/drafting");
        mimes.put(".dump", "application/octet-stream");
        mimes.put(".dv", "video/x-dv");
        mimes.put(".dvi", "application/x-dvi");
        mimes.put(".dwf", "drawing/x-dwf (old)");
        mimes.put(".dwf", "model/vnd.dwf");
        mimes.put(".dwg", "application/acad");
        mimes.put(".dwg", "image/vnd.dwg");
        mimes.put(".dwg", "image/x-dwg");
        mimes.put(".dxf", "application/dxf");
        mimes.put(".dxf", "image/vnd.dwg");
        mimes.put(".dxf", "image/x-dwg");
        mimes.put(".dxr", "application/x-director");
        mimes.put(".el", "text/x-script.elisp");
        mimes.put(".elc", "application/x-bytecode.elisp (compiled elisp)");
        mimes.put(".elc", "application/x-elc");
        mimes.put(".env", "application/x-envoy");
        mimes.put(".eps", "application/postscript");
        mimes.put(".es", "application/x-esrehber");
        mimes.put(".etx", "text/x-setext");
        mimes.put(".evy", "application/envoy");
        mimes.put(".evy", "application/x-envoy");
        mimes.put(".exe", "application/octet-stream");
        mimes.put(".f", "text/plain");
        mimes.put(".f", "text/x-fortran");
        mimes.put(".f77", "text/x-fortran");
        mimes.put(".f90", "text/plain");
        mimes.put(".f90", "text/x-fortran");
        mimes.put(".fdf", "application/vnd.fdf");
        mimes.put(".fif", "application/fractals");
        mimes.put(".fif", "image/fif");
        mimes.put(".fli", "video/fli");
        mimes.put(".fli", "video/x-fli");
        mimes.put(".flo", "image/florian");
        mimes.put(".flx", "text/vnd.fmi.flexstor");
        mimes.put(".fmf", "video/x-atomic3d-feature");
        mimes.put(".for", "text/plain");
        mimes.put(".for", "text/x-fortran");
        mimes.put(".fpx", "image/vnd.fpx");
        mimes.put(".fpx", "image/vnd.net-fpx");
        mimes.put(".frl", "application/freeloader");
        mimes.put(".funk", "audio/make");
        mimes.put(".g", "text/plain");
        mimes.put(".g3", "image/g3fax");
        mimes.put(".gif", "image/gif");
        mimes.put(".gl", "video/gl");
        mimes.put(".gl", "video/x-gl");
        mimes.put(".gsd", "audio/x-gsm");
        mimes.put(".gsm", "audio/x-gsm");
        mimes.put(".gsp", "application/x-gsp");
        mimes.put(".gss", "application/x-gss");
        mimes.put(".gtar", "application/x-gtar");
        mimes.put(".gz", "application/x-compressed");
        mimes.put(".gz", "application/x-gzip");
        mimes.put(".gzip", "application/x-gzip");
        mimes.put(".gzip", "multipart/x-gzip");
        mimes.put(".h", "text/plain");
        mimes.put(".h", "text/x-h");
        mimes.put(".hdf", "application/x-hdf");
        mimes.put(".help", "application/x-helpfile");
        mimes.put(".hgl", "application/vnd.hp-hpgl");
        mimes.put(".hh", "text/plain");
        mimes.put(".hh", "text/x-h");
        mimes.put(".hlb", "text/x-script");
        mimes.put(".hlp", "application/hlp");
        mimes.put(".hlp", "application/x-helpfile");
        mimes.put(".hlp", "application/x-winhelp");
        mimes.put(".hpg", "application/vnd.hp-hpgl");
        mimes.put(".hpgl", "application/vnd.hp-hpgl");
        mimes.put(".hqx", "application/binhex");
        mimes.put(".hqx", "application/binhex4");
        mimes.put(".hqx", "application/mac-binhex");
        mimes.put(".hqx", "application/mac-binhex40");
        mimes.put(".hqx", "application/x-binhex40");
        mimes.put(".hqx", "application/x-mac-binhex40");
        mimes.put(".hta", "application/hta");
        mimes.put(".htc", "text/x-component");
        mimes.put(".htm", "text/html");
        mimes.put(".html", "text/html");
        mimes.put(".htmls", "text/html");
        mimes.put(".htt", "text/webviewhtml");
        mimes.put(".htx", "text/html");
        mimes.put(".ice", "x-conference/x-cooltalk");
        mimes.put(".ico", "image/x-icon");
        mimes.put(".idc", "text/plain");
        mimes.put(".ief", "image/ief");
        mimes.put(".iefs", "image/ief");
        mimes.put(".iges", "application/iges");
        mimes.put(".iges", "model/iges");
        mimes.put(".igs", "application/iges");
        mimes.put(".igs", "model/iges");
        mimes.put(".ima", "application/x-ima");
        mimes.put(".imap", "application/x-httpd-imap");
        mimes.put(".inf", "application/inf");
        mimes.put(".ins", "application/x-internett-signup");
        mimes.put(".ip", "application/x-ip2");
        mimes.put(".isu", "video/x-isvideo");
        mimes.put(".it", "audio/it");
        mimes.put(".iv", "application/x-inventor");
        mimes.put(".ivr", "i-world/i-vrml");
        mimes.put(".ivy", "application/x-livescreen");
        mimes.put(".jam", "audio/x-jam");
        mimes.put(".jav", "text/plain");
        mimes.put(".jav", "text/x-java-source");
        mimes.put(".java", "text/plain");
        mimes.put(".java", "text/x-java-source");
        mimes.put(".jcm", "application/x-java-commerce");
        mimes.put(".jfif", "image/jpeg");
        mimes.put(".jfif", "image/pjpeg");
        mimes.put(".jfif-tbnl", "image/jpeg");
        mimes.put(".jpe", "image/jpeg");
        mimes.put(".jpe", "image/pjpeg");
        mimes.put(".jpeg", "image/jpeg");
        mimes.put(".jpeg", "image/pjpeg");
        mimes.put(".jpg", "image/jpeg");
        mimes.put(".jpg", "image/pjpeg");
        mimes.put(".jps", "image/x-jps");
        mimes.put(".js", "application/x-javascript");
        mimes.put(".js", "application/javascript");
        mimes.put(".js", "application/ecmascript");
        mimes.put(".js", "text/javascript");
        mimes.put(".js", "text/ecmascript");
        mimes.put(".jut", "image/jutvision");
        mimes.put(".kar", "audio/midi");
        mimes.put(".kar", "music/x-karaoke");
        mimes.put(".ksh", "application/x-ksh");
        mimes.put(".ksh", "text/x-script.ksh");
        mimes.put(".la", "audio/nspaudio");
        mimes.put(".la", "audio/x-nspaudio");
        mimes.put(".lam", "audio/x-liveaudio");
        mimes.put(".latex", "application/x-latex");
        mimes.put(".lha", "application/lha");
        mimes.put(".lha", "application/octet-stream");
        mimes.put(".lha", "application/x-lha");
        mimes.put(".lhx", "application/octet-stream");
        mimes.put(".list", "text/plain");
        mimes.put(".lma", "audio/nspaudio");
        mimes.put(".lma", "audio/x-nspaudio");
        mimes.put(".log", "text/plain");
        mimes.put(".lsp", "application/x-lisp");
        mimes.put(".lsp", "text/x-script.lisp");
        mimes.put(".lst", "text/plain");
        mimes.put(".lsx", "text/x-la-asf");
        mimes.put(".ltx", "application/x-latex");
        mimes.put(".lzh", "application/octet-stream");
        mimes.put(".lzh", "application/x-lzh");
        mimes.put(".lzx", "application/lzx");
        mimes.put(".lzx", "application/octet-stream");
        mimes.put(".lzx", "application/x-lzx");
        mimes.put(".m", "text/plain");
        mimes.put(".m", "text/x-m");
        mimes.put(".m1v", "video/mpeg");
        mimes.put(".m2a", "audio/mpeg");
        mimes.put(".m2v", "video/mpeg");
        mimes.put(".m3u", "audio/x-mpequrl");
        mimes.put(".man", "application/x-troff-man");
        mimes.put(".map", "application/x-navimap");
        mimes.put(".mar", "text/plain");
        mimes.put(".mbd", "application/mbedlet");
        mimes.put(".mc$", "application/x-magic-cap-package-1.0");
        mimes.put(".mcd", "application/mcad");
        mimes.put(".mcd", "application/x-mathcad");
        mimes.put(".mcf", "image/vasa");
        mimes.put(".mcf", "text/mcf");
        mimes.put(".mcp", "application/netmc");
        mimes.put(".me", "application/x-troff-me");
        mimes.put(".mht", "message/rfc822");
        mimes.put(".mhtml", "message/rfc822");
        mimes.put(".mid", "application/x-midi");
        mimes.put(".mid", "audio/midi");
        mimes.put(".mid", "audio/x-mid");
        mimes.put(".mid", "audio/x-midi");
        mimes.put(".mid", "music/crescendo");
        mimes.put(".mid", "x-music/x-midi");
        mimes.put(".midi", "application/x-midi");
        mimes.put(".midi", "audio/midi");
        mimes.put(".midi", "audio/x-mid");
        mimes.put(".midi", "audio/x-midi");
        mimes.put(".midi", "music/crescendo");
        mimes.put(".midi", "x-music/x-midi");
        mimes.put(".mif", "application/x-frame");
        mimes.put(".mif", "application/x-mif");
        mimes.put(".mime", "message/rfc822");
        mimes.put(".mime", "www/mime");
        mimes.put(".mjf", "audio/x-vnd.audioexplosion.mjuicemediafile");
        mimes.put(".mjpg", "video/x-motion-jpeg");
        mimes.put(".mm", "application/base64");
        mimes.put(".mm", "application/x-meme");
        mimes.put(".mme", "application/base64");
        mimes.put(".mod", "audio/mod");
        mimes.put(".mod", "audio/x-mod");
        mimes.put(".moov", "video/quicktime");
        mimes.put(".mov", "video/quicktime");
        mimes.put(".movie", "video/x-sgi-movie");
        mimes.put(".mp2", "audio/mpeg");
        mimes.put(".mp2", "audio/x-mpeg");
        mimes.put(".mp2", "video/mpeg");
        mimes.put(".mp2", "video/x-mpeg");
        mimes.put(".mp2", "video/x-mpeq2a");
        mimes.put(".mp3", "audio/mpeg3");
        mimes.put(".mp3", "audio/x-mpeg-3");
        mimes.put(".mp3", "video/mpeg");
        mimes.put(".mp3", "video/x-mpeg");
        mimes.put(".mpa", "audio/mpeg");
        mimes.put(".mpa", "video/mpeg");
        mimes.put(".mpc", "application/x-project");
        mimes.put(".mpe", "video/mpeg");
        mimes.put(".mpeg", "video/mpeg");
        mimes.put(".mpg", "audio/mpeg");
        mimes.put(".mpg", "video/mpeg");
        mimes.put(".mpga", "audio/mpeg");
        mimes.put(".mpp", "application/vnd.ms-project");
        mimes.put(".mpt", "application/x-project");
        mimes.put(".mpv", "application/x-project");
        mimes.put(".mpx", "application/x-project");
        mimes.put(".mrc", "application/marc");
        mimes.put(".ms", "application/x-troff-ms");
        mimes.put(".mv", "video/x-sgi-movie");
        mimes.put(".my", "audio/make");
        mimes.put(".mzz", "application/x-vnd.audioexplosion.mzz");
        mimes.put(".nap", "image/naplps");
        mimes.put(".naplps", "image/naplps");
        mimes.put(".nc", "application/x-netcdf");
        mimes.put(".ncm", "application/vnd.nokia.configuration-message");
        mimes.put(".nif", "image/x-niff");
        mimes.put(".niff", "image/x-niff");
        mimes.put(".nix", "application/x-mix-transfer");
        mimes.put(".nsc", "application/x-conference");
        mimes.put(".nvd", "application/x-navidoc");
        mimes.put(".o", "application/octet-stream");
        mimes.put(".oda", "application/oda");
        mimes.put(".omc", "application/x-omc");
        mimes.put(".omcd", "application/x-omcdatamaker");
        mimes.put(".omcr", "application/x-omcregerator");
        mimes.put(".p", "text/x-pascal");
        mimes.put(".p10", "application/pkcs10");
        mimes.put(".p10", "application/x-pkcs10");
        mimes.put(".p12", "application/pkcs-12");
        mimes.put(".p12", "application/x-pkcs12");
        mimes.put(".p7a", "application/x-pkcs7-signature");
        mimes.put(".p7c", "application/pkcs7-mime");
        mimes.put(".p7c", "application/x-pkcs7-mime");
        mimes.put(".p7m", "application/pkcs7-mime");
        mimes.put(".p7m", "application/x-pkcs7-mime");
        mimes.put(".p7r", "application/x-pkcs7-certreqresp");
        mimes.put(".p7s", "application/pkcs7-signature");
        mimes.put(".part", "application/pro_eng");
        mimes.put(".pas", "text/pascal");
        mimes.put(".pbm", "image/x-portable-bitmap");
        mimes.put(".pcl", "application/vnd.hp-pcl");
        mimes.put(".pcl", "application/x-pcl");
        mimes.put(".pct", "image/x-pict");
        mimes.put(".pcx", "image/x-pcx");
        mimes.put(".pdb", "chemical/x-pdb");
        mimes.put(".pdf", "application/pdf");
        mimes.put(".pfunk", "audio/make");
        mimes.put(".pfunk", "audio/make.my.funk");
        mimes.put(".pgm", "image/x-portable-graymap");
        mimes.put(".pgm", "image/x-portable-greymap");
        mimes.put(".pic", "image/pict");
        mimes.put(".pict", "image/pict");
        mimes.put(".pkg", "application/x-newton-compatible-pkg");
        mimes.put(".pko", "application/vnd.ms-pki.pko");
        mimes.put(".pl", "text/plain");
        mimes.put(".pl", "text/x-script.perl");
        mimes.put(".plx", "application/x-pixclscript");
        mimes.put(".pm", "image/x-xpixmap");
        mimes.put(".pm", "text/x-script.perl-module");
        mimes.put(".pm4", "application/x-pagemaker");
        mimes.put(".pm5", "application/x-pagemaker");
        mimes.put(".png", "image/png");
        mimes.put(".pnm", "application/x-portable-anymap");
        mimes.put(".pnm", "image/x-portable-anymap");
        mimes.put(".pot", "application/mspowerpoint");
        mimes.put(".pot", "application/vnd.ms-powerpoint");
        mimes.put(".pov", "model/x-pov");
        mimes.put(".ppa", "application/vnd.ms-powerpoint");
        mimes.put(".ppm", "image/x-portable-pixmap");
        mimes.put(".pps", "application/mspowerpoint");
        mimes.put(".pps", "application/vnd.ms-powerpoint");
        mimes.put(".ppt", "application/mspowerpoint");
        mimes.put(".ppt", "application/powerpoint");
        mimes.put(".ppt", "application/vnd.ms-powerpoint");
        mimes.put(".ppt", "application/x-mspowerpoint");
        mimes.put(".ppz", "application/mspowerpoint");
        mimes.put(".pre", "application/x-freelance");
        mimes.put(".prt", "application/pro_eng");
        mimes.put(".ps", "application/postscript");
        mimes.put(".psd", "application/octet-stream");
        mimes.put(".pvu", "paleovu/x-pv");
        mimes.put(".pwz", "application/vnd.ms-powerpoint");
        mimes.put(".py", "text/x-script.phyton");
        mimes.put(".pyc", "application/x-bytecode.python");
        mimes.put(".qcp", "audio/vnd.qcelp");
        mimes.put(".qd3", "x-world/x-3dmf");
        mimes.put(".qd3d", "x-world/x-3dmf");
        mimes.put(".qif", "image/x-quicktime");
        mimes.put(".qt", "video/quicktime");
        mimes.put(".qtc", "video/x-qtc");
        mimes.put(".qti", "image/x-quicktime");
        mimes.put(".qtif", "image/x-quicktime");
        mimes.put(".ra", "audio/x-pn-realaudio");
        mimes.put(".ra", "audio/x-pn-realaudio-plugin");
        mimes.put(".ra", "audio/x-realaudio");
        mimes.put(".ram", "audio/x-pn-realaudio");
        mimes.put(".ras", "application/x-cmu-raster");
        mimes.put(".ras", "image/cmu-raster");
        mimes.put(".ras", "image/x-cmu-raster");
        mimes.put(".rast", "image/cmu-raster");
        mimes.put(".rexx", "text/x-script.rexx");
        mimes.put(".rf", "image/vnd.rn-realflash");
        mimes.put(".rgb", "image/x-rgb");
        mimes.put(".rm", "application/vnd.rn-realmedia");
        mimes.put(".rm", "audio/x-pn-realaudio");
        mimes.put(".rmi", "audio/mid");
        mimes.put(".rmm", "audio/x-pn-realaudio");
        mimes.put(".rmp", "audio/x-pn-realaudio");
        mimes.put(".rmp", "audio/x-pn-realaudio-plugin");
        mimes.put(".rng", "application/ringing-tones");
        mimes.put(".rng", "application/vnd.nokia.ringing-tone");
        mimes.put(".rnx", "application/vnd.rn-realplayer");
        mimes.put(".roff", "application/x-troff");
        mimes.put(".rp", "image/vnd.rn-realpix");
        mimes.put(".rpm", "audio/x-pn-realaudio-plugin");
        mimes.put(".rt", "text/richtext");
        mimes.put(".rt", "text/vnd.rn-realtext");
        mimes.put(".rtf", "application/rtf");
        mimes.put(".rtf", "application/x-rtf");
        mimes.put(".rtf", "text/richtext");
        mimes.put(".rtx", "application/rtf");
        mimes.put(".rtx", "text/richtext");
        mimes.put(".rv", "video/vnd.rn-realvideo");
        mimes.put(".s", "text/x-asm");
        mimes.put(".s3m", "audio/s3m");
        mimes.put(".saveme", "application/octet-stream");
        mimes.put(".sbk", "application/x-tbook");
        mimes.put(".scm", "application/x-lotusscreencam");
        mimes.put(".scm", "text/x-script.guile");
        mimes.put(".scm", "text/x-script.scheme");
        mimes.put(".scm", "video/x-scm");
        mimes.put(".sdml", "text/plain");
        mimes.put(".sdp", "application/sdp");
        mimes.put(".sdp", "application/x-sdp");
        mimes.put(".sdr", "application/sounder");
        mimes.put(".sea", "application/sea");
        mimes.put(".sea", "application/x-sea");
        mimes.put(".set", "application/set");
        mimes.put(".sgm", "text/sgml");
        mimes.put(".sgm", "text/x-sgml");
        mimes.put(".sgml", "text/sgml");
        mimes.put(".sgml", "text/x-sgml");
        mimes.put(".sh", "application/x-bsh");
        mimes.put(".sh", "application/x-sh");
        mimes.put(".sh", "application/x-shar");
        mimes.put(".sh", "text/x-script.sh");
        mimes.put(".shar", "application/x-bsh");
        mimes.put(".shar", "application/x-shar");
        mimes.put(".shtml", "text/html");
        mimes.put(".shtml", "text/x-server-parsed-html");
        mimes.put(".sid", "audio/x-psid");
        mimes.put(".sit", "application/x-sit");
        mimes.put(".sit", "application/x-stuffit");
        mimes.put(".skd", "application/x-koan");
        mimes.put(".skm", "application/x-koan");
        mimes.put(".skp", "application/x-koan");
        mimes.put(".skt", "application/x-koan");
        mimes.put(".sl", "application/x-seelogo");
        mimes.put(".smi", "application/smil");
        mimes.put(".smil", "application/smil");
        mimes.put(".snd", "audio/basic");
        mimes.put(".snd", "audio/x-adpcm");
        mimes.put(".sol", "application/solids");
        mimes.put(".spc", "application/x-pkcs7-certificates");
        mimes.put(".spc", "text/x-speech");
        mimes.put(".spl", "application/futuresplash");
        mimes.put(".spr", "application/x-sprite");
        mimes.put(".sprite", "application/x-sprite");
        mimes.put(".src", "application/x-wais-source");
        mimes.put(".ssi", "text/x-server-parsed-html");
        mimes.put(".ssm", "application/streamingmedia");
        mimes.put(".sst", "application/vnd.ms-pki.certstore");
        mimes.put(".step", "application/step");
        mimes.put(".stl", "application/sla");
        mimes.put(".stl", "application/vnd.ms-pki.stl");
        mimes.put(".stl", "application/x-navistyle");
        mimes.put(".stp", "application/step");
        mimes.put(".sv4cpio", "application/x-sv4cpio");
        mimes.put(".sv4crc", "application/x-sv4crc");
        mimes.put(".svf", "image/vnd.dwg");
        mimes.put(".svf", "image/x-dwg");
        mimes.put(".svr", "application/x-world");
        mimes.put(".svr", "x-world/x-svr");
        mimes.put(".swf", "application/x-shockwave-flash");
        mimes.put(".t", "application/x-troff");
        mimes.put(".talk", "text/x-speech");
        mimes.put(".tar", "application/x-tar");
        mimes.put(".tbk", "application/toolbook");
        mimes.put(".tbk", "application/x-tbook");
        mimes.put(".tcl", "application/x-tcl");
        mimes.put(".tcl", "text/x-script.tcl");
        mimes.put(".tcsh", "text/x-script.tcsh");
        mimes.put(".tex", "application/x-tex");
        mimes.put(".texi", "application/x-texinfo");
        mimes.put(".texinfo", "application/x-texinfo");
        mimes.put(".text", "application/plain");
        mimes.put(".text", "text/plain");
        mimes.put(".tgz", "application/gnutar");
        mimes.put(".tgz", "application/x-compressed");
        mimes.put(".tif", "image/tiff");
        mimes.put(".tif", "image/x-tiff");
        mimes.put(".tiff", "image/tiff");
        mimes.put(".tiff", "image/x-tiff");
        mimes.put(".tr", "application/x-troff");
        mimes.put(".tsi", "audio/tsp-audio");
        mimes.put(".tsp", "application/dsptype");
        mimes.put(".tsp", "audio/tsplayer");
        mimes.put(".tsv", "text/tab-separated-values");
        mimes.put(".turbot", "image/florian");
        mimes.put(".txt", "text/plain");
        mimes.put(".uil", "text/x-uil");
        mimes.put(".uni", "text/uri-list");
        mimes.put(".unis", "text/uri-list");
        mimes.put(".unv", "application/i-deas");
        mimes.put(".uri", "text/uri-list");
        mimes.put(".uris", "text/uri-list");
        mimes.put(".ustar", "application/x-ustar");
        mimes.put(".ustar", "multipart/x-ustar");
        mimes.put(".uu", "application/octet-stream");
        mimes.put(".uu", "text/x-uuencode");
        mimes.put(".uue", "text/x-uuencode");
        mimes.put(".vcd", "application/x-cdlink");
        mimes.put(".vcs", "text/x-vcalendar");
        mimes.put(".vda", "application/vda");
        mimes.put(".vdo", "video/vdo");
        mimes.put(".vew", "application/groupwise");
        mimes.put(".viv", "video/vivo");
        mimes.put(".viv", "video/vnd.vivo");
        mimes.put(".vivo", "video/vivo");
        mimes.put(".vivo", "video/vnd.vivo");
        mimes.put(".vmd", "application/vocaltec-media-desc");
        mimes.put(".vmf", "application/vocaltec-media-file");
        mimes.put(".voc", "audio/voc");
        mimes.put(".voc", "audio/x-voc");
        mimes.put(".vos", "video/vosaic");
        mimes.put(".vox", "audio/voxware");
        mimes.put(".vqe", "audio/x-twinvq-plugin");
        mimes.put(".vqf", "audio/x-twinvq");
        mimes.put(".vql", "audio/x-twinvq-plugin");
        mimes.put(".vrml", "application/x-vrml");
        mimes.put(".vrml", "model/vrml");
        mimes.put(".vrml", "x-world/x-vrml");
        mimes.put(".vrt", "x-world/x-vrt");
        mimes.put(".vsd", "application/x-visio");
        mimes.put(".vst", "application/x-visio");
        mimes.put(".vsw", "application/x-visio");
        mimes.put(".w60", "application/wordperfect6.0");
        mimes.put(".w61", "application/wordperfect6.1");
        mimes.put(".w6w", "application/msword");
        mimes.put(".wav", "audio/wav");
        mimes.put(".wav", "audio/x-wav");
        mimes.put(".wb1", "application/x-qpro");
        mimes.put(".wbmp", "image/vnd.wap.wbmp");
        mimes.put(".web", "application/vnd.xara");
        mimes.put(".wiz", "application/msword");
        mimes.put(".wk1", "application/x-123");
        mimes.put(".wmf", "windows/metafile");
        mimes.put(".wml", "text/vnd.wap.wml");
        mimes.put(".wmlc", "application/vnd.wap.wmlc");
        mimes.put(".wmls", "text/vnd.wap.wmlscript");
        mimes.put(".wmlsc", "application/vnd.wap.wmlscriptc");
        mimes.put(".word", "application/msword");
        mimes.put(".wp", "application/wordperfect");
        mimes.put(".wp5", "application/wordperfect");
        mimes.put(".wp5", "application/wordperfect6.0");
        mimes.put(".wp6", "application/wordperfect");
        mimes.put(".wpd", "application/wordperfect");
        mimes.put(".wpd", "application/x-wpwin");
        mimes.put(".wq1", "application/x-lotus");
        mimes.put(".wri", "application/mswrite");
        mimes.put(".wri", "application/x-wri");
        mimes.put(".wrl", "application/x-world");
        mimes.put(".wrl", "model/vrml");
        mimes.put(".wrl", "x-world/x-vrml");
        mimes.put(".wrz", "model/vrml");
        mimes.put(".wrz", "x-world/x-vrml");
        mimes.put(".wsc", "text/scriplet");
        mimes.put(".wsrc", "application/x-wais-source");
        mimes.put(".wtk", "application/x-wintalk");
        mimes.put(".xbm", "image/x-xbitmap");
        mimes.put(".xbm", "image/x-xbm");
        mimes.put(".xbm", "image/xbm");
        mimes.put(".xdr", "video/x-amt-demorun");
        mimes.put(".xgz", "xgl/drawing");
        mimes.put(".xif", "image/vnd.xiff");
        mimes.put(".xl", "application/excel");
        mimes.put(".xla", "application/excel");
        mimes.put(".xla", "application/x-excel");
        mimes.put(".xla", "application/x-msexcel");
        mimes.put(".xlb", "application/excel");
        mimes.put(".xlb", "application/vnd.ms-excel");
        mimes.put(".xlb", "application/x-excel");
        mimes.put(".xlc", "application/excel");
        mimes.put(".xlc", "application/vnd.ms-excel");
        mimes.put(".xlc", "application/x-excel");
        mimes.put(".xld", "application/excel");
        mimes.put(".xld", "application/x-excel");
        mimes.put(".xlk", "application/excel");
        mimes.put(".xlk", "application/x-excel");
        mimes.put(".xll", "application/excel");
        mimes.put(".xll", "application/vnd.ms-excel");
        mimes.put(".xll", "application/x-excel");
        mimes.put(".xlm", "application/excel");
        mimes.put(".xlm", "application/vnd.ms-excel");
        mimes.put(".xlm", "application/x-excel");
        mimes.put(".xls", "application/excel");
        mimes.put(".xls", "application/vnd.ms-excel");
        mimes.put(".xls", "application/x-excel");
        mimes.put(".xls", "application/x-msexcel");
        mimes.put(".xlt", "application/excel");
        mimes.put(".xlt", "application/x-excel");
        mimes.put(".xlv", "application/excel");
        mimes.put(".xlv", "application/x-excel");
        mimes.put(".xlw", "application/excel");
        mimes.put(".xlw", "application/vnd.ms-excel");
        mimes.put(".xlw", "application/x-excel");
        mimes.put(".xlw", "application/x-msexcel");
        mimes.put(".xm", "audio/xm");
        mimes.put(".xml", "application/xml");
        mimes.put(".xml", "text/xml");
        mimes.put(".xmz", "xgl/movie");
        mimes.put(".xpix", "application/x-vnd.ls-xpix");
        mimes.put(".xpm", "image/x-xpixmap");
        mimes.put(".xpm", "image/xpm");
        mimes.put(".x-png", "image/png");
        mimes.put(".xsr", "video/x-amt-showrun");
        mimes.put(".xwd", "image/x-xwd");
        mimes.put(".xwd", "image/x-xwindowdump");
        mimes.put(".xyz", "chemical/x-pdb");
        mimes.put(".z", "application/x-compress");
        mimes.put(".z", "application/x-compressed");
        mimes.put(".zip", "application/x-compressed");
        mimes.put(".zip", "application/x-zip-compressed");
        mimes.put(".zip", "application/zip");
        mimes.put(".zip", "multipart/x-zip");
        mimes.put(".zoo", "application/octet-stream");
        mimes.put(".zsh", "text/x-script.zsh");

    }
}
