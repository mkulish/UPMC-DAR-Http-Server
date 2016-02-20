package edu.upmc.dar.server.servlet;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.HttpVersion;
import edu.upmc.dar.server.common.enumeration.ResponseCode;
import edu.upmc.dar.server.request.HttpRequest;
import edu.upmc.dar.server.response.HttpResponse;
import edu.upmc.dar.server.util.HtmlProducerUtil;
import edu.upmc.dar.server.util.JsonUtil;

/**
 * Processes the request, generates and sends back the response using the passed response object
 */
public class HttpServlet {
    protected ContentType contentType;

    /**
     * The main method which produces the response given the request
     * Normally needs to be overriden as it sends back by default the request data in the response
     * @param request to be processed
     * @param response object to be modified
     */
    public void serve(HttpRequest request, HttpResponse response) throws Exception {
        //Generating the response body in the corresponding format
        switch (response.getContentType()) {
            case TEXT_PLAIN: {
                response.setBody(request.toString());
                break;
            }
            case TEXT_HTML: {
                response.setBody(HtmlProducerUtil.generateHtmlTable(request));
                break;
            }
            case APP_JSON: {
                response.setBody(JsonUtil.serializeRequest(request));
                break;
            }
        }
    }

    public final HttpResponse processRequest(HttpRequest request) throws Exception {
        HttpResponse response = buildBaseResponse(request);

        serve(request, response);

        return response;
    }

    protected HttpResponse buildBaseResponse(HttpRequest request){
        HttpResponse response = new HttpResponse(HttpVersion.v1_1, ResponseCode.OK);

        //Using text/plain content type by default
        if(contentType != null){
            response.setContentType(contentType);
        } else if(request.getContentType() != null) {
            response.setContentType(request.getContentType());
        } else {
            response.setContentType(ContentType.TEXT_PLAIN);
        }

        //Writing the content type to the response header
        response.getHeader().getParamsMap().put("Content-Type", response.getContentType().getName());

        return response;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
