package edu.upmc.dar.server.html.page;

import edu.upmc.dar.server.html.HtmlProducer;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.http.request.HttpRequest;

public class NotFoundPage extends HtmlProducer {
    private static NotFoundPage instance;
    private NotFoundPage(){}

    public static NotFoundPage instance() {
        if (instance == null) {
            instance = new NotFoundPage();
        }
        return instance;
    }
    
    @Override
    protected String body(Model model, HttpRequest request){
        return  "<h1>404 Not Found</h1>\n" +
                "<h3><i>Requested page is not found</i></h3>\n";
    }
}
