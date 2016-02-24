package edu.upmc.dar.server.common.enumeration;

public enum ContentType {
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    APP_JSON("application/json");

    private String name;

    ContentType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static ContentType getContentType(String name){
        for(ContentType elem : values()){
            if(elem.getName().equalsIgnoreCase(name)){
                return elem;
            }
        }
        return TEXT_HTML;
    }

    @Override
    public String toString(){
        return getName();
    }
}
