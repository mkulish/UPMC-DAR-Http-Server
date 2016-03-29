package edu.upmc.dar.applications.chat.service.impl;

import edu.upmc.dar.applications.chat.entity.Message;
import edu.upmc.dar.applications.chat.service.MessagesService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MessagesServiceImpl implements MessagesService {
    private static MessagesServiceImpl instance;
    private MessagesServiceImpl(){}

    public static synchronized MessagesServiceImpl instance(){
        if(instance == null){
            instance = new MessagesServiceImpl();
        }
        return instance;
    }

    private List<Message> list = new LinkedList<>();
    private int nextIdentity = 0;

    @Override
    public synchronized List<Message> getAll() {
        List<Message> listCopy = new ArrayList<>(list.size());
        list.forEach(message -> listCopy.add(message.copy()));
        return listCopy;
    }

    @Override
    public synchronized List<Message> getAfter(Integer id) {
        List<Message> listCopy = new ArrayList<>(list.size());
        list.forEach(message -> {if(message.getId() > id){listCopy.add(message.copy());}});
        return listCopy;
    }

    @Override
    public synchronized Message get(Integer id) {
        if(id == null) return null;
        return list.stream().filter(message -> id.equals(message.getId())).findFirst().orElse(null);
    }

    @Override
    public synchronized Message saveOrUpdate(Message message) {
        if(message != null){
            Message existing = get(message.getId());
            if(existing != null){
                //Updating the point
                existing.setSender(message.getSender());
                existing.setText(message.getText());
                existing.setDatetime(message.getDatetime());
                message.setId(existing.getId());
                return existing;
            } else {
                //Saving the new point
                Message newMessage = message.copy();
                newMessage.setId(nextIdentity++);
                message.setId(newMessage.getId());
                list.add(newMessage);
                return newMessage;
            }
        }

        return null;
    }

    @Override
    public synchronized void delete(Message message) {
        if(message != null){
            delete(message.getId());
        }
    }

    @Override
    public synchronized void delete(Integer id) {
        Message existing = get(id);
        if(existing != null){
            list.remove(existing);
        }
    }
}
