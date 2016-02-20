package edu.upmc.dar.server.response;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.HttpVersion;
import edu.upmc.dar.server.common.enumeration.ResponseCode;

public class HttpResponse {
    private HttpVersion version;
    private ResponseCode responseCode;

    private HttpResponseHeader header = new HttpResponseHeader();

    private String body;
    private ContentType contentType;

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
            header.getParamsMap().put("Content-Type",contentType.getName());
        }
    }


    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(version.toString()).append(" ").append(responseCode.getCode())
                                         .append(" ").append(responseCode.getMessage()).append('\n');

        result.append(header.toString()).append('\n');
        result.append('\n');
        if(body != null) {
            result.append(body).append('\n');
        }

        return result.toString();
    }
}
