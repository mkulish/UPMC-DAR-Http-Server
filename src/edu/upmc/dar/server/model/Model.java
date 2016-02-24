package edu.upmc.dar.server.model;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<String, Object> attributesMap = new HashMap<>();

    public Model(){}
    public Model(Map<String, Object> attributes){
        attributesMap.putAll(attributes);
    }

    public Object get(String attributeName){
        return attributesMap.get(attributeName);
    }

    public void put(String name, Object value){
        attributesMap.put(name, value);
    }
}
