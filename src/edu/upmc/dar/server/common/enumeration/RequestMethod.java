package edu.upmc.dar.server.common.enumeration;

public enum RequestMethod {
    GET, POST, PUT, DELETE, OPTIONS, HEAD, TRACE, CONNECT, ANY;

    public static RequestMethod getMethod(String methodName){
        for(RequestMethod elem : values()){
            if(elem.toString().equalsIgnoreCase(methodName)){
                return elem;
            }
        }
        return ANY;
    }
}
