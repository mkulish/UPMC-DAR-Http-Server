package edu.upmc.dar.applications.chat.servlet.page;


import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/chat/login", method = RequestMethod.GET)
public class LoginPageServlet extends HttpServlet {
    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        return "chat/auth/login";
    }
}
