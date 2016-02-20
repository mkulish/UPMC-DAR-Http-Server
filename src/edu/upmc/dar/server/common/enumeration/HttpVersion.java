package edu.upmc.dar.server.common.enumeration;

public enum HttpVersion {
    v1_0("HTTP/1.0"),
    v1_1("HTTP/1.1");

    private String name;

    HttpVersion(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static HttpVersion getVersion(String versionName){
        for(HttpVersion elem : values()){
            if(elem.toString().equalsIgnoreCase(versionName)){
                return elem;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return getName();
    }
}
