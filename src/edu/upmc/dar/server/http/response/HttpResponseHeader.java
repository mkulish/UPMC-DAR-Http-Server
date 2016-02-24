package edu.upmc.dar.server.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {
    private Map<String, String> paramsMap = new HashMap<>();

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        for(String paramName : paramsMap.keySet()){
            result.append(paramName).append(": ").append(paramsMap.get(paramName)).append('\n');
        }

        return result.toString();
    }
}
