package edu.upmc.dar.applications.chat.servlet.chat;


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
import edu.upmc.dar.server.util.Log;

@Servlet(url = "/chat/events.stream", method = RequestMethod.GET, produces = ContentType.EVENT_STREAM)
public class EventSubscriptionServlet extends HttpServlet {
    private ChatMemberService chatMemberService = ChatMemberServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        ChatMember member = (ChatMember) request.getSession().getAttribute("chatMember");

        if(member != null){
//            if(member.getSocketWriter() != null) {
//                member.getSocketWriter().flush();
//                member.getSocketWriter().close();
//            }
            member.setSocketWriter(response.getWriter());

            Log.debug("Replacing socket writer for " + member.getId());
            chatMemberService.saveOrUpdate(member);
            request.getSession().setAttribute("chatMember", member);

            response.getHeader().getParamsMap().put("Cache-Control", "no-cache");
            response.getHeader().getParamsMap().put("Connection", "keep-alive");
            String responseHeader = response.toString();
            response.getWriter().write(responseHeader);
            response.getWriter().flush();
        }

        return null;
    }
}
