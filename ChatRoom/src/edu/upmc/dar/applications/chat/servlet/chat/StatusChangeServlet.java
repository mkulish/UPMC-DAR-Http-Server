package edu.upmc.dar.applications.chat.servlet.chat;


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
import edu.upmc.dar.server.util.JsonUtil;

@Servlet(url = "/chat/change-status2.do", method = RequestMethod.POST, produces = ContentType.APP_JSON)
public class StatusChangeServlet extends HttpServlet {
    private ChatMemberService chatMemberService = ChatMemberServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        int id = Integer.valueOf(request.getParam("id"));
        ChatMemberStatus status = ChatMemberStatus.values()[Integer.valueOf(request.getParam("status"))];

        ChatMember member = chatMemberService.get(id);
        if(member != null){
            member.setStatus(status);
            chatMemberService.saveOrUpdate(member);
            chatMemberService.broadcastStatusChanged(member.getId(), member.getStatus());
            return JsonUtil.serializeSubmitResponse(true, "");
        } else {
            return JsonUtil.serializeSubmitResponse(false, "Chat member not found");
        }
    }
}
