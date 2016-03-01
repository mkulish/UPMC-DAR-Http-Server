package edu.upmc.dar.server.dispatch;

import edu.upmc.dar.server.common.exception.MultipleMappingsException;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.servlet.HttpServlet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServletContainer {
    private static final Pattern URL_PARAMS_PATTERN = Pattern.compile("\\{[^/]+\\}");

    private Map<RequestMatch, HttpServlet> servletsMap = new HashMap<>();

    public synchronized void registerServlet(RequestMatch matchingCriteria, HttpServlet servlet){
        servletsMap.put(matchingCriteria, servlet);
    }

    public synchronized void unregisterServlet(RequestMatch matchingCriteria){
        servletsMap.remove(matchingCriteria);
    }

    /**
     * Searches for the corresponding servlet
     * @param request to be processed
     * @return servlet that matches the request or null if no servlet found
     * @throws MultipleMappingsException if multiple mappings were found
     */
    public synchronized HttpServlet processRequest(HttpRequest request) throws MultipleMappingsException {
        HttpServlet result = null;

        for(Map.Entry<RequestMatch, HttpServlet> matching : servletsMap.entrySet()){
            if(matching.getKey().matches(request) && checkUrlParams(matching.getKey(), request)){
                if(result != null){
                    throw new MultipleMappingsException(request.toString());
                }
                result = matching.getValue();
            }
        }
        return result;
    }

    private boolean checkUrlParams(RequestMatch match, HttpRequest request){
        //Url params
        String requestUrl = normalize(request.getUrl());
        String matchUrl = normalize(match.getUrl());

        String[] urlParts = requestUrl.split("/");

        List<String> requiredParamNames = new LinkedList<>();
        Matcher matcher = URL_PARAMS_PATTERN.matcher(match.getUrl());
        while(matcher.find()){
            String value = matcher.group();
            int partIndex = countChar(matchUrl.substring(0, matcher.start()), '/') - requiredParamNames.size();
            if(partIndex < urlParts.length){
                String paramName = value.substring(1, value.length() - 1);
                requiredParamNames.add(paramName);

                request.setUrlParam(paramName, urlParts[partIndex]);
            } else {
                return false;
            }
        }
        return true;
    }

    private String normalize(String value){
        String result = value.trim();
        if(result.startsWith("/")){
            result = result.substring(1);
        }
        if(result.endsWith("/")){
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private int countChar(String str, char ch){
        int result = 0;
        for(char strChar : str.toCharArray()){
            if(ch == strChar){
                result++;
            }
        }
        return result;
    }
}
