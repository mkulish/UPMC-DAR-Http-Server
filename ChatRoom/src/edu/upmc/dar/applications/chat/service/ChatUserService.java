package edu.upmc.dar.applications.chat.service;


import edu.upmc.dar.applications.chat.entity.ChatUser;

import java.util.List;

public interface ChatUserService {
    List<ChatUser> getAll();
    ChatUser get(Integer id);
    ChatUser get(String login);
    ChatUser get(String login, String passhash);
    ChatUser saveOrUpdate(ChatUser point);
    void delete(ChatUser point);
    void delete(Integer id);
}
