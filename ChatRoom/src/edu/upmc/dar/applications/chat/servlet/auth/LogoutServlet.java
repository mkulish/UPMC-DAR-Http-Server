package edu.upmc.dar.applications.chat.servlet.auth;


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

@Servlet(url = "/chat/logout.do", method = RequestMethod.POST)
public class LogoutServlet extends HttpServlet {
    private ChatMemberService chatMemberService = ChatMemberServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        ChatUser user = (ChatUser) request.getSession().getAttribute("user");

        if(user != null){
            request.getSession().setAttribute("user", null);
            request.getSession().setAttribute("chatMember", null);
            ChatMember member = chatMemberService.get(user.getId());
            if(member != null){
                member.setStatus(ChatMemberStatus.OFFLINE);
                chatMemberService.saveOrUpdate(member);
                chatMemberService.broadcastStatusChanged(member.getId(), member.getStatus());
            }
            return "redirect:/chat";
        } else {
            // already logged out
            return "redirect:/chat";
        }
    }
}
