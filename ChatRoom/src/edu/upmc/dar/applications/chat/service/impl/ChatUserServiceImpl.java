package edu.upmc.dar.applications.chat.service.impl;

import edu.upmc.dar.applications.chat.entity.ChatUser;
import edu.upmc.dar.applications.chat.service.ChatUserService;
import edu.upmc.dar.server.security.entity.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatUserServiceImpl implements ChatUserService {
    private static ChatUserServiceImpl instance;
    private ChatUserServiceImpl(){}

    public static synchronized ChatUserServiceImpl instance(){
        if(instance == null){
            instance = new ChatUserServiceImpl();
        }
        return instance;
    }

    private List<ChatUser> list = new LinkedList<>();
    private int nextIdentity = 0;

    @Override
    public synchronized List<ChatUser> getAll() {
        List<ChatUser> listCopy = new ArrayList<>(list.size());
        list.forEach(point -> listCopy.add(point.copy()));
        return listCopy;
    }

    @Override
    public synchronized ChatUser get(Integer id) {
        if(id == null) return null;
        return list.stream().filter(user -> id.equals(user.getId())).findFirst().orElse(null);
    }

    @Override
     public synchronized ChatUser get(String login) {
        if(login == null) return null;
        return list.stream().filter(user -> login.equals(user.getLogin())).findFirst().orElse(null);
    }

    @Override
    public synchronized ChatUser get(String login, String passhash) {
        if(login == null) return null;
        return list.stream().filter(user -> login.equals(user.getLogin()) && passhash.equals(user.getPassword())).findFirst().orElse(null);
    }

    @Override
    public synchronized ChatUser saveOrUpdate(ChatUser user) {
        if(user != null){
            ChatUser existing = get(user.getId());
            if(existing != null){
                //Updating the point
                existing.setLogin(user.getLogin());
                existing.setPassword(user.getPassword());
                user.setId(existing.getId());
                return existing;
            } else {
                //Saving the new point
                ChatUser newUser = user.copy();
                newUser.setId(nextIdentity++);
                user.setId(newUser.getId());
                list.add(newUser);
                return newUser;
            }
        }

        return null;
    }

    @Override
    public synchronized void delete(ChatUser user) {
        if(user != null){
            delete(user.getId());
        }
    }

    @Override
    public synchronized void delete(Integer id) {
        User existing = get(id);
        if(existing != null){
            list.remove(existing);
        }
    }
}
