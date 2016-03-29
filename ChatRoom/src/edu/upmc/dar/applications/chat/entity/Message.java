package edu.upmc.dar.applications.chat.entity;

import java.util.Date;

public class Message {
    private Integer id;
    private ChatUser sender;
    private String text;
    private Date datetime;

    public Message(ChatUser sender, String text) {
        this.sender = sender;
        this.text = text;
        this.datetime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public boolean equals(Object obj){
        return (obj instanceof Message && ((Message)obj).id.equals(id));
    }

    public Message copy(){
        Message result = new Message(sender, text);
        result.setDatetime(datetime);
        result.setId(id);
        return result;
    }
}
