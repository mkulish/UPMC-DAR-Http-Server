package edu.upmc.dar.applications.chat.servlet.auth;


import edu.upmc.dar.applications.chat.entity.ChatUser;
import edu.upmc.dar.applications.chat.service.ChatUserService;
import edu.upmc.dar.applications.chat.service.impl.ChatUserServiceImpl;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.security.entity.User;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/chat/signup.do", method = RequestMethod.POST)
public class SignupServlet extends HttpServlet {
    ChatUserService userService = ChatUserServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        String login = request.getParam("login");
        String passhash = request.getParam("passhash");

        if(userService.get(login) == null){
            ChatUser newUser = new ChatUser(new User(login, passhash));
            userService.saveOrUpdate(newUser);

            request.getSession().setAttribute("user", newUser);
            return "redirect:/chat";
        } else {
            model.put("signupUserExists", true);
            return "chat/auth/login";
        }
    }
}
