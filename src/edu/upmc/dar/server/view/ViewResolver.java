package edu.upmc.dar.server.view;

import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.model.Model;

import java.util.HashMap;
import java.util.Map;

public class ViewResolver {
    private static ViewResolver instance;

    public static ViewResolver instance(){
        if(instance == null){
            instance = new ViewResolver();
        }
        return instance;
    }

    private Map<String, Template> templatesMap = new HashMap<>();

    public synchronized void registerTemplate(String fullName, Template instance){
        templatesMap.put(fullName, instance);
    }

    public synchronized void unregisterTemplate(String name){
        templatesMap.remove(name);
    }

    public synchronized Template getView(String name){
        return templatesMap.get(name);
    }

    public synchronized Template getViewToInclude(String name){
        return (templatesMap.containsKey(name))? templatesMap.get(name) : new Template() {
            @Override
            public String doView(Model model, HttpRequest request) {
                return "";
            }
        };
    }
}
