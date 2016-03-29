package edu.upmc.dar.applications.chat.service;


import edu.upmc.dar.applications.chat.common.enums.ChatMemberStatus;
import edu.upmc.dar.applications.chat.entity.ChatMember;

import java.util.List;

public interface ChatMemberService {
    ChatMember get(int id);
    List<ChatMember> getAll();
    ChatMember saveOrUpdate(ChatMember member);
    void delete(ChatMember member);
    void delete(int id);
    void broadcastMessage(int senderId, String message, String data);
    void broadcastStatusChanged(int senderId, ChatMemberStatus status);
    void broadcastMessageSent(int senderId, String message);
}
