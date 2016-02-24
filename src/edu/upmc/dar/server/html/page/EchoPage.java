package edu.upmc.dar.server.html.page;

import edu.upmc.dar.server.html.HtmlProducer;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.http.request.HttpRequest;

import java.util.Map;

public class EchoPage extends HtmlProducer {
    private static EchoPage instance;
    private EchoPage(){}

    public static EchoPage instance(){
        if(instance == null){
            instance = new EchoPage();
        }
        return instance;
    }

    @Override
    protected String style(Model model, HttpRequest request){
        return  "#mainTable{\n" +
                    "font-size: 16;\n" +
                "}\n" +
                "#mainTable td {\n" +
                    "min-width: 400px;\n" +
                "}\n" +
                "#mainTable tr.header {\n" +
                    "height: 40px;\n" +
                    "font-weight: bold;\n" +
                    "font-size: 20;\n" +
                "}\n";
    }
    
    @Override
    protected String body(Model model, HttpRequest request){
        StringBuilder html = new StringBuilder();

        html.append("<span><h1>Received HTTP request data</h1></span>\n");

        //Request data
        html.append("<table id=\"mainTable\">\n");
        html.append("<tr><td colspan=\"2\"><h3> </h3></td></tr>\n");

        //Request status line
        html.append("<tr><td><i>Method</i></td><td>").append(request.getMethod()).append("</td></tr>\n");
        html.append("<tr><td><i>URL</i></td><td>").append(request.getUrl()).append("</td></tr>\n");
        html.append("<tr><td><i>Version</i></td><td>").append(request.getVersion()).append("</td></tr>\n");

        //Request header
        html.append("<tr><td colspan=\"2\"><h3>Header</h3></td></tr>\n");
        for(Map.Entry<String, String> param : request.getHeader().getParamsMap().entrySet()){
            html.append("<tr><td><i>").append(param.getKey()).append("</i></td><td>")
                                      .append(param.getValue()).append("</td></tr>\n");
        }

        //Request body
        if(request.getBody() != null) {
            html.append("<tr><td colspan=\"2\"><h3>Request body</h3></td></tr>\n");
            html.append("<tr><td colspan=\"2\">").append(request.getBody()).append("</td></tr>\n");
        }

        html.append("</table>\n");

        return html.toString();
    }
}
