package edu.upmc.dar.server.view;

import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.model.Model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Template {
    protected Map<String, List<Component>> componentsMap = new HashMap<>();

    public abstract String doView(Model model, HttpRequest request);

    public void includeComponent(String name, StringBuilder out, Model model, HttpRequest request){
        List<Component> components = componentsMap.get(name);
        if(components != null){
            int count = 0;
            for(Component component : components) {
                if(count++ > 0){
                    out.append("\n");
                }
                component.doView(out, model, request);
            }
        }
    }

    public Template includeTemplate(String name){
        return ViewResolver.instance().getViewToInclude(name);
    }

    public Template putComponent(String name, Component component){
        componentsMap.put(name, new LinkedList<>());
        componentsMap.get(name).add(component);
        return this;
    }

    public interface Component{
        void doView(StringBuilder out, Model model, HttpRequest request);
    }
}
