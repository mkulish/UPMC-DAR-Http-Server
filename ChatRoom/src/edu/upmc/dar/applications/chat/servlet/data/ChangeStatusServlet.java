package edu.upmc.dar.applications.chat.servlet.data;


import edu.upmc.dar.applications.chat.common.enums.ChatMemberStatus;
import edu.upmc.dar.applications.chat.entity.ChatMember;
import edu.upmc.dar.applications.chat.service.ChatMemberService;
import edu.upmc.dar.applications.chat.service.impl.ChatMemberServiceImpl;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/chat/change-status.do", method = RequestMethod.POST, produces = ContentType.APP_JSON)
public class ChangeStatusServlet extends HttpServlet {
    //No dependencies injection for the moment
    private ChatMemberService chatMemberService = ChatMemberServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        String status = request.getParam("status");
        ChatMember member = (ChatMember) request.getSession().getAttribute("chatMember");

        if(member != null && status != null && ! "".equals(status)){
            member.setStatus(ChatMemberStatus.values()[Integer.valueOf(status)]);
            chatMemberService.saveOrUpdate(member);
            return edu.upmc.dar.server.util.JsonUtil.serializeSubmitResponse(true, "");
        } else {
            return edu.upmc.dar.server.util.JsonUtil.serializeSubmitResponse(false, "Chat member not found");
        }
    }
}
