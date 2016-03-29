package edu.upmc.dar.applications.chat.service;


import edu.upmc.dar.applications.chat.entity.Message;

import java.util.List;

public interface MessagesService {
    List<Message> getAll();
    Message get(Integer id);
    List<Message> getAfter(Integer id);
    Message saveOrUpdate(Message point);
    void delete(Message message);
    void delete(Integer id);
}
