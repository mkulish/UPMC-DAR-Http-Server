package edu.upmc.dar.server.dispatch;

import edu.upmc.dar.server.common.exception.MultipleMappingsException;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.servlet.HttpServlet;

import java.util.HashMap;
import java.util.Map;

public class ServletContainer {
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
            if(matching.getKey().matches(request)){
                if(result != null){
                    throw new MultipleMappingsException(request.toString());
                }
                result = matching.getValue();
            }
        }
        return result;
    }
}
