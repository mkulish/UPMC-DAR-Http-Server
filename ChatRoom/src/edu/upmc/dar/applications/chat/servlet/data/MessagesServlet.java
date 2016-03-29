package edu.upmc.dar.applications.chat.servlet.data;


import edu.upmc.dar.applications.chat.service.MessagesService;
import edu.upmc.dar.applications.chat.service.impl.MessagesServiceImpl;
import edu.upmc.dar.applications.chat.util.JsonUtil;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/chat/messages.data", method = RequestMethod.GET, produces = ContentType.APP_JSON)
public class MessagesServlet extends HttpServlet {
    //No dependencies injection for the moment
    private MessagesService messagesService = MessagesServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        String id = request.getParam("last");
        if(id == null || "".equals(id)){
            return JsonUtil.serializeMessages(messagesService.getAll());
        } else {
            return JsonUtil.serializeMessages(messagesService.getAfter(Integer.valueOf(id)));
        }
    }
}
