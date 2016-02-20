package edu.upmc.dar.server.request;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.HttpVersion;
import edu.upmc.dar.server.common.enumeration.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private RequestMethod method;
    private HttpVersion version;
    private String url;

    private HttpRequestHeader header = new HttpRequestHeader();
    private Map<String, String> paramsMap = new HashMap<>();
    private String body;

    private ContentType contentType;



    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpRequestHeader getHeader() {
        return header;
    }

    public void setHeader(HttpRequestHeader header) {
        this.header = header;
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
    }

    public void setParam(String paramName, String value){
        paramsMap.put(paramName, value);
    }

    public String getParam(String paramName){
        return paramsMap.get(paramName);
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(method.name()).append(" ").append(url).append(" ").append(version).append('\n');

        result.append(header.toString()).append('\n');
        result.append('\n');
        if(body != null) {
            result.append(body).append('\n');
        }

        return result.toString();
    }
}
