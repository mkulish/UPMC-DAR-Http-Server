package edu.upmc.dar.applications.chat.entity;

import edu.upmc.dar.server.security.entity.User;

public class ChatUser extends User {
    public ChatUser(User user){
        super(user);
    }

    public ChatUser copy(){
        return new ChatUser(super.copy());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ChatUser) && super.equals(obj);
    }
}
