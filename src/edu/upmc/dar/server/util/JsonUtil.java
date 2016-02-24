package edu.upmc.dar.server.util;

import edu.upmc.dar.server.http.request.HttpRequest;

import java.util.Map;

public class JsonUtil {

    public static String serializeRequest(HttpRequest request) throws Exception {
        StringBuilder result = new StringBuilder();

        result.append("{");

        result.append("\"method\":\"").append(request.getMethod()).append("\"");
        result.append(",\"url\":\"").append(request.getUrl()).append("\"");
        result.append(",\"version\":\"").append(request.getVersion()).append("\"");

        StringBuilder header = new StringBuilder();
        for(Map.Entry<String, String> param : request.getHeader().getParamsMap().entrySet()){
            header.append(",\"").append(param.getKey()).append("\":\"").append(param.getValue()).append("\"");
        }
        result.append(",\"header\":{").append(header.substring(1)).append("}");

        result.append(",\"body\":\"").append(request.getBody()).append("\"");

        result.append("}");
        return result.toString();
    }

    public static String serializeSubmitResponse(boolean success, String message) throws Exception {
        return "{\"result\":\"" + success + "\",\"message\":\"" + message + "\"}";
    }
}
