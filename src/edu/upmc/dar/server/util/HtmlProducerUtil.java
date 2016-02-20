package edu.upmc.dar.server.util;

import edu.upmc.dar.server.request.HttpRequest;

import java.util.Map;

public class HtmlProducerUtil {

    public static String generateHtmlTable(HttpRequest request){
        StringBuilder responseBody = new StringBuilder();

        //Page beginning
        responseBody.append("<html>\n");
        responseBody.append("<head>\n");
        responseBody.append("<style>\n");
        responseBody.append("#mainTable{\n");
        responseBody.append("font-size: 16;\n");
        responseBody.append("}\n");
        responseBody.append("#mainTable td {\n");
        responseBody.append("min-width: 400px;\n");
        responseBody.append("}\n");
        responseBody.append("#mainTable tr.header {\n");
        responseBody.append("height: 40px;\n");
        responseBody.append("font-weight: bold;\n");
        responseBody.append("font-size: 20;\n");
        responseBody.append("}\n");
        responseBody.append("</style>\n");
        responseBody.append("</head>\n");
        responseBody.append("<body>\n");
        responseBody.append("<div>\n");
        responseBody.append("<span><h1>Received HTTP request data</h1></span>\n");

        //Request data
        responseBody.append("<table id=\"mainTable\">\n");
        responseBody.append("<tr><td colspan=\"2\"><h3> </h3></td></tr>\n");

        //Status line
        responseBody.append("<tr><td><i>Method</i></td><td>").append(request.getMethod()).append("</td></tr>\n");
        responseBody.append("<tr><td><i>URL</i></td><td>").append(request.getUrl()).append("</td></tr>\n");
        responseBody.append("<tr><td><i>Version</i></td><td>").append(request.getVersion()).append("</td></tr>\n");

        //Header
        responseBody.append("<tr><td colspan=\"2\"><h3>Header</h3></td></tr>\n");
        for(Map.Entry<String, String> param : request.getHeader().getParamsMap().entrySet()){
            responseBody.append("<tr><td><i>").append(param.getKey()).append("</i></td><td>")
                    .append(param.getValue()).append("</td></tr>\n");
        }

        //Body
        if(request.getBody() != null) {
            responseBody.append("<tr><td colspan=\"2\"><h3>Request body</h3></td></tr>\n");
            responseBody.append("<tr><td colspan=\"2\">").append(request.getBody()).append("</td></tr>\n");
        }

        //Rest of the page
        responseBody.append("</table>\n");

        responseBody.append("</div>\n");
        responseBody.append("</body>\n");
        responseBody.append("</html>\n");

        return responseBody.toString();
    }
}
