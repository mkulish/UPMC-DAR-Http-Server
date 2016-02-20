package edu.upmc.dar.server.dispatch;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.request.HttpRequest;

public class RequestMatch {
    private RequestMethod method;
    private ContentType contentType;
    private String urlPattern;

    public RequestMatch(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public RequestMatch setMethod(RequestMethod method) {
        this.method = method;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public RequestMatch setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    /**
     * Checks if the request matches the criteria
     * @param request to be checked
     * @return true if matches, false otherwise
     */
    public boolean matches(HttpRequest request){
        //Comparing the method
        if(method != RequestMethod.ANY && request.getMethod() != RequestMethod.ANY
                && method != null && method != request.getMethod()){
            return false;
        }

        //Comparing the content type
        if(contentType != null && contentType != request.getContentType()){
            return false;
        }

        //URL check
        return request.getUrl().matches(this.urlPattern);
    }

    @Override
    public int hashCode(){
        int result = urlPattern.hashCode();
        if(method != null){
            result *= method.ordinal() + 1;
        }
        if(contentType != null){
            result *= contentType.ordinal() + 1;
        }
        return result;
    }

    @Override
    public boolean equals(Object otherObj){
        if(! (otherObj instanceof RequestMatch)) return false;
        RequestMatch other = (RequestMatch) otherObj;

        return (other.urlPattern.equals(this.urlPattern) &&
        ( (this.method == null && other.method == null) || this.method == other.method) &&
        ( (this.contentType == null && other.contentType == null) || this.contentType == other.contentType));
    }
}
