package edu.upmc.dar.server.servlet;

import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.HttpVersion;
import edu.upmc.dar.server.common.enumeration.ResponseCode;
import edu.upmc.dar.server.http.common.Cookie;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.http.session.Session;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.view.Template;
import edu.upmc.dar.server.view.ViewResolver;

import java.io.PrintWriter;

/**
 * Processes the request, generates and sends back the response using the passed response object
 */
public abstract class HttpServlet {
    protected ContentType contentType;

    /**
     * The main method which produces the response given the request
     * Normally needs to be overriden as it sends back by default the request data in the response
     * @param request to be processed
     * @param response object to be modified
     */
    public abstract String serve(HttpRequest request, HttpResponse response, Model model) throws Exception;

    public final HttpResponse processRequest(HttpRequest request, PrintWriter writer) throws Exception {
        HttpResponse response = buildBaseResponse(request);
        response.setWriter(writer);

        Model model = new Model();
        String returnedValue = serve(request, response, model);

        if(response.getContentType() == ContentType.EVENT_STREAM) return response;

        if(returnedValue != null && returnedValue.startsWith("redirect:")) {
            String url = returnedValue.substring("redirect:".length());
            response.setResponseCode(ResponseCode.REDIRECT);
            response.getHeader().getParamsMap().put("Location", url);
        } else {

            if (response.getContentType() == null) {
                response.setContentType(ContentType.TEXT_HTML);
            }

            if (response.getContentType() == ContentType.TEXT_HTML) {
                Template template = ViewResolver.instance().getView(returnedValue);
                if (template != null) {
                    response.setBody(template.doView(model, request));
                } else if (response.getBody() == null || "".equals(response.getBody())) {
                    response.setBody(returnedValue);
                }
            } else {
                if (response.getBody() == null || "".equals(response.getBody())) {
                    response.setBody(returnedValue);
                }
            }

        }

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
        response.getHeader().getParamsMap().put("Content-Type", response.getContentType().getName()+";charset=UTF-8");

        //Creating new session
        if(request.isSessionExpired()){
            Cookie sessionCookie = new Cookie("sessionToken", request.getSession().getToken());
            sessionCookie.setMaxAge(60 * Session.EXPIRY_IN_MINUTES);
            response.addCookie(sessionCookie);
        }

        return response;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
