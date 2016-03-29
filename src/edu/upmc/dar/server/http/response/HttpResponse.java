package edu.upmc.dar.server.http.response;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.HttpVersion;
import edu.upmc.dar.server.common.enumeration.ResponseCode;
import edu.upmc.dar.server.http.common.Cookie;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private HttpVersion version;
    private ResponseCode responseCode;

    private HttpResponseHeader header = new HttpResponseHeader();
    private Map<String, Cookie> cookiesMap = new HashMap<>();

    private String body;
    private ContentType contentType;

    private PrintWriter writer;

    public HttpResponse(HttpVersion version, ResponseCode responseCode) {
        this.version = version;
        this.responseCode = responseCode;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public HttpResponseHeader getHeader() {
        return header;
    }

    public void setHeader(HttpResponseHeader header) {
        this.header = header;
    }

    public void addCookie(Cookie cookie){
        cookiesMap.put(cookie.getName(), cookie);
    }

    public Cookie getCookie(String paramName){
        return cookiesMap.get(paramName);
    }

    public List<String> getCookieNames(){
        return new LinkedList<>(cookiesMap.keySet());
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
        if(header != null){
            header.getParamsMap().put("Content-Type",contentType.getName()+";charset=UTF-8");
        }
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(version.toString()).append(" ").append(responseCode.getCode())
                                         .append(" ").append(responseCode.getMessage()).append('\n');

        result.append(header.toString());
        for(String cookieName : cookiesMap.keySet()){
            result.append("Set-Cookie: ").append(cookiesMap.get(cookieName)).append('\n');
        }
        result.append('\n');

        if(body != null) {
            result.append(body).append('\n');
        }

        return result.toString();
    }
}
