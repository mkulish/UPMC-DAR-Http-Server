package edu.upmc.dar.server.servlet.page;


import edu.upmc.dar.server.html.page.NotFoundPage;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;

public class NotFoundPageServlet extends HttpServlet {

    @Override
    public void serve(HttpRequest request, HttpResponse response) throws Exception {
        response.setBody(NotFoundPage.instance().produceHtml(request));
    }
}
