package edu.upmc.dar.applications.chat.servlet.data;


import edu.upmc.dar.applications.chat.entity.ChatMember;
import edu.upmc.dar.applications.chat.entity.Message;
import edu.upmc.dar.applications.chat.service.MessagesService;
import edu.upmc.dar.applications.chat.service.impl.MessagesServiceImpl;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/chat/send-message.do", method = RequestMethod.POST, produces = ContentType.APP_JSON)
public class SendMessageServlet extends HttpServlet {
    //No dependencies injection for the moment
    private MessagesService messagesService = MessagesServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        String text = request.getParam("text");
        ChatMember member = (ChatMember) request.getSession().getAttribute("chatMember");

        if(member != null && text != null && ! "".equals(text)){
            Message message = new Message(member, text);
            messagesService.saveOrUpdate(message);
            return edu.upmc.dar.server.util.JsonUtil.serializeSubmitResponse(true, "");
        } else {
            return edu.upmc.dar.server.util.JsonUtil.serializeSubmitResponse(false, "Empty message text!");
        }
    }
}
