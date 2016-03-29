package edu.upmc.dar.applications.chat.servlet.data;


import edu.upmc.dar.applications.chat.service.ChatMemberService;
import edu.upmc.dar.applications.chat.service.impl.ChatMemberServiceImpl;
import edu.upmc.dar.applications.chat.util.JsonUtil;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/chat/statuses.data", method = RequestMethod.GET, produces = ContentType.APP_JSON)
public class StatusesServlet extends HttpServlet {
    //No dependencies injection for the moment
    private ChatMemberService chatMemberService = ChatMemberServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        return JsonUtil.serializeStatuses(chatMemberService.getAll());
    }
}
