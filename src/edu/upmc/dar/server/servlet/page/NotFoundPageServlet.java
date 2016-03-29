package edu.upmc.dar.server.servlet.page;


import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

public class NotFoundPageServlet extends HttpServlet {

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        return "templates/not-found";
    }
}
