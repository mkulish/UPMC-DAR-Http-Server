package edu.upmc.dar.applications.chat.util;

import edu.upmc.dar.applications.chat.entity.ChatMember;
import edu.upmc.dar.applications.chat.entity.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class JsonUtil {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String serializeStatuses(List<ChatMember> members) throws Exception {
        StringBuilder result = new StringBuilder();

        result.append("[");

        boolean first = true;
        for(ChatMember member : members){
            if(! first){
                result.append(",");
            } else {
                first = false;
            }
            result.append("{\"id\":").append(member.getId())
                    .append(",\"status\":").append(member.getStatus().ordinal())
                    .append(",\"login\":\"").append(member.getLogin()).append("\"")
                    .append("}");
        }
        result.append("]");
        return result.toString();
    }

    public static String serializeMessages(List<Message> messages) throws Exception {
        StringBuilder result = new StringBuilder();

        result.append("[");

        boolean first = true;
        for(Message message : messages){
            if(! first){
                result.append(",");
            } else {
                first = false;
            }
            result.append(serializeMessage(message));
        }
        result.append("]");
        return result.toString();
    }

    public static String serializeMessage(Message message){
        StringBuilder result = new StringBuilder();
        result.append("{");
        result.append("\"id\":").append(message.getId());
        result.append(",\"text\":\"").append(message.getText().replace("\n","<br/>")).append("\"");
        result.append(",\"datetime\":\"").append(df.format(message.getDatetime())).append("\"");
        if(message.getSender() != null) {
            result.append(",\"sender\":{")
                    .append("\"id\":").append(message.getSender().getId())
                    .append(",\"login\":\"").append(message.getSender().getLogin()).append("\"}");
        }
        result.append("}");
        return result.toString();
    }
}
