package edu.upmc.dar.server.html.page;

import edu.upmc.dar.server.html.HtmlProducer;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.model.Model;

public class InternalErrorPage extends HtmlProducer {
    private static InternalErrorPage instance;
    private InternalErrorPage(){}

    public static InternalErrorPage instance() {
        if (instance == null) {
            instance = new InternalErrorPage();
        }
        return instance;
    }
    
    @Override
    protected String body(Model model, HttpRequest request){
        return  "<h1>500 Internal Server Error</h1>\n" +
                "<h3><i>An internal server error has occurred, please try again later or contact the support team.</i></h3>\n";
    }
}
