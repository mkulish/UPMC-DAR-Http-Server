package edu.upmc.dar.server.common.enumeration;

public enum ResponseCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    REDIRECT(302, "Found");

    private int code;
    private String message;

    ResponseCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
}
