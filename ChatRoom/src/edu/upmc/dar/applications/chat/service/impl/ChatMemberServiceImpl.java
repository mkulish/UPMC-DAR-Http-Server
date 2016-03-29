package edu.upmc.dar.applications.chat.service.impl;

import edu.upmc.dar.applications.chat.common.enums.ChatMemberStatus;
import edu.upmc.dar.applications.chat.entity.ChatMember;
import edu.upmc.dar.applications.chat.service.ChatMemberService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatMemberServiceImpl implements ChatMemberService {
    private Map<Integer, ChatMember> list = new HashMap<>();
    private static ChatMemberServiceImpl instance;
    private int messageId = 0;

    public static ChatMemberServiceImpl instance(){
        if(instance == null){
            instance = new ChatMemberServiceImpl();
//            instance.launchBroadcastingLoop();
        }
        return instance;
    }

    private void launchBroadcastingLoop(){
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(500);
                        broadcastMessage(-1, "ping", "-");
                    } catch (InterruptedException ex){
                        //nothing
                    }
                }
            }
        }.start();
    }

    @Override
    public synchronized ChatMember get(int id) {
        return list.get(id);
    }

    @Override
    public List<ChatMember> getAll() {
        ArrayList<ChatMember> result = new ArrayList<>(list.entrySet().size());
        for(ChatMember member : list.values()){
            result.add(member.copy());
        }
        return result;
    }

    @Override
    public synchronized ChatMember saveOrUpdate(ChatMember member) {
        return list.put(member.getId(), member.copy());
    }

    @Override
    public synchronized void delete(ChatMember member) {
        list.remove(member.getId());
    }

    @Override
    public synchronized void delete(int id) {
        list.remove(id);
    }

    @Override
    public synchronized void broadcastMessage(int senderId, String message, String data) {
        for(ChatMember member : list.values()){
            if(senderId != member.getId()){
                member.sendMessage(messageId, message, data);
            }
        }
        messageId++;
    }

    @Override
    public synchronized void broadcastStatusChanged(int senderId, ChatMemberStatus status) {
        broadcastMessage(senderId, "status", "{\"id\":" + senderId + ",\"status\":" + status.ordinal() + "}");
    }

    @Override
    public synchronized void broadcastMessageSent(int senderId, String message) {
        broadcastMessage(senderId, "chat", "{\"id\":" + senderId + ",\"message\":\"" + message + "\"}");
    }
}
