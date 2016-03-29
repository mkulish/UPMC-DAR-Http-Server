package edu.upmc.dar.applications.chat.servlet.page;


import edu.upmc.dar.applications.chat.common.enums.ChatMemberStatus;
import edu.upmc.dar.applications.chat.entity.ChatMember;
import edu.upmc.dar.applications.chat.entity.ChatUser;
import edu.upmc.dar.applications.chat.service.ChatMemberService;
import edu.upmc.dar.applications.chat.service.impl.ChatMemberServiceImpl;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

import java.util.List;

@Servlet(url = "/chat", method = RequestMethod.GET)
public class ChatMainPageServlet extends HttpServlet {
    //No dependencies injection for the moment
    private ChatMemberService chatMemberService = ChatMemberServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        List<ChatMember> members = chatMemberService.getAll();

        ChatMember member = (ChatMember) request.getSession().getAttribute("chatMember");

        if(member == null){
            ChatUser user = (ChatUser) request.getSession().getAttribute("user");
            if(user != null){
                member = new ChatMember(user, null);
                member.setStatus(ChatMemberStatus.ONLINE);
                chatMemberService.saveOrUpdate(member);
                chatMemberService.broadcastStatusChanged(member.getId(), member.getStatus());
                request.getSession().setAttribute("chatMember", member);
            }
        }

        if(member != null){
            members.remove(member);
        }
        model.put("members", members);

        return "chat/main/chat-page";
    }
}
