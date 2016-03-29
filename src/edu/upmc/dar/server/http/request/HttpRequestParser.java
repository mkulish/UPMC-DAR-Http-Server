package edu.upmc.dar.server.http.request;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.HttpVersion;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.common.Cookie;
import edu.upmc.dar.server.http.session.SessionUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpRequestParser {

    public static HttpRequest parseRequest(Socket socket) throws Exception {
        InputStream inputStream = socket.getInputStream();

        HttpRequest request = new HttpRequest();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            //Parsing the first line - method, url and protocol version
            line = reader.readLine();
            if(line == null || "".equals(line)){
                return request;
            }
            String[] data = line.split(" ");

            request.setMethod(RequestMethod.getMethod(data[0]));
            request.setUrl(data[1]);
            request.setVersion(HttpVersion.getVersion(data[2]));

            //Parsing the request header
            while ((line = reader.readLine()) != null && ! line.equals("")) {
                data = line.split(":");
                request.getHeader().getParamsMap().put(data[0].trim(), data[1].trim());
            }

            //Parsing the request body
            //Avoid reading more than "Content-Length" bytes to not get blocked by the socket "read" method
            String lengthParam = request.getHeader().getParamsMap().get("Content-Length");

            if (lengthParam != null) {
                int length = Integer.parseInt(lengthParam);

                StringBuilder requestBody = new StringBuilder();
                for (int i = 0; i < length; ++i) {
                    requestBody.append((char) reader.read());
                }
                request.setBody(requestBody.toString());
                
                System.out.println(requestBody.toString());
            }

            //Additional initialization
            request.setContentType(ContentType.getContentType(request.getHeader().getParamsMap().get("Accept")));
            request.setIp(socket.getInetAddress());
            
            if(request.getHeader().getParamsMap().get("User-Agent") == null){
            	request.getHeader().getParamsMap().put("User-Agent", "");
            }

            //Cookies
            String cookiesHeader = request.getHeader().getParamsMap().get("Cookie");
            if(cookiesHeader != null && ! "".equals(cookiesHeader)){
                String[] cookies = cookiesHeader.split(";");
                for(String cookie : cookies) {
                    String[] parsedCookie = cookie.split("=");
                    if(parsedCookie.length == 2) {
                        request.addCookie(new Cookie(parsedCookie[0], parsedCookie[1]));
                    }
                }
            }

            //Session
            SessionUtil.checkSession(request);

            //Params parsing - if no files are uploaded
            String contentType = request.getHeader().getParamsMap().get("Content-Type");
            if (contentType != null && contentType.toLowerCase().contains("multipart")) {
                //if the request has multipart content type - do nothing for the moment
                //as it needs to be parsed another way
                return request;
            }

            //we are only parsing simple "GET-style" params line like "param1=value1&param2=value2.."
            String paramsLine = null;
            if (request.getMethod() == RequestMethod.POST) {
                paramsLine = request.getBody();
            } else {
                if (request.getUrl().contains("?")) {
                    paramsLine = request.getUrl().substring(request.getUrl().indexOf("?") + 1);
                    request.setUrl(request.getUrl().substring(0, request.getUrl().indexOf("?")));
                }
            }

            if (paramsLine != null) {
                data = paramsLine.split("&");
                for (String keyValuePair : data) {
                    String[] param = keyValuePair.split("=");
                    if (param.length == 2) {
                        request.setParam(param[0].trim(), param[1].trim());
                    }
                }
            }
        } catch (Exception ex) {
            // we will try to return what we have already parsed in order to serve the request if possible
            ex.printStackTrace();
        }

        return request;
    }
}
