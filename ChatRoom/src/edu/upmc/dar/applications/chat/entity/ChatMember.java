package edu.upmc.dar.applications.chat.entity;

import edu.upmc.dar.applications.chat.common.enums.ChatMemberStatus;
import edu.upmc.dar.server.util.Log;

import java.io.PrintWriter;

public class ChatMember extends ChatUser {
    private PrintWriter socketWriter;
    private ChatMemberStatus status = ChatMemberStatus.OFFLINE;

    public synchronized boolean sendMessage(int id, String message, String data){
        if(socketWriter == null) {
            Log.debug("No socket writer for the member " + getId());
            return false;
        }

        try{
            socketWriter.write("id:" + id + "\n");
            socketWriter.write("event:" + message + "\n");
//            socketWriter.write("retry: 3000\n");
            socketWriter.write("data:" + data + "\n\n");
//            socketWriter.flush();
            Log.debug("Send " + message + " message to " + getId());
        } catch (Exception ex){
            Log.err(ex.getMessage());
            return false;
        }
        return true;
    }

    public ChatMember copy(){
        ChatMember result = new ChatMember(super.copy(), this.socketWriter);
        result.setStatus(status);
        return result;
    }

    public ChatMember(ChatUser user, PrintWriter socketWriter) {
        super(user);
        this.socketWriter = socketWriter;
        this.status = ChatMemberStatus.OFFLINE;
    }

    public synchronized PrintWriter getSocketWriter() {
        return socketWriter;
    }

    public synchronized void setSocketWriter(PrintWriter socketWriter) {
        this.socketWriter = socketWriter;
    }

    public ChatMemberStatus getStatus() {
        return status;
    }

    public void setStatus(ChatMemberStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ChatMember) && super.equals(obj);
    }
}
