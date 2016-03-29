package edu.upmc.dar.applications.chat.servlet.auth;


import edu.upmc.dar.applications.chat.entity.ChatUser;
import edu.upmc.dar.applications.chat.service.ChatUserService;
import edu.upmc.dar.applications.chat.service.impl.ChatUserServiceImpl;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/chat/login.do", method = RequestMethod.POST)
public class LoginServlet extends HttpServlet {
    ChatUserService userService = ChatUserServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        String login = request.getParam("login");
        String passhash = request.getParam("passhash");

        ChatUser user = userService.get(login, passhash);
        if(user != null){
            request.getSession().setAttribute("user", user);
            return "redirect:/chat";
        } else {
            model.put("loginFailed", true);
            return "chat/auth/login";
        }
    }
}