package edu.upmc.dar.server.html;

import edu.upmc.dar.server.security.entity.User;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.http.request.HttpRequest;

public abstract class HtmlProducer {
    public final String produceHtml(Model model, HttpRequest request){
        return layout(model, request);
    }

    public final String produceHtml(HttpRequest request){
        return layout(new Model(), request);
    }

    protected final String layout(Model model, HttpRequest request){

        return "<html>" +
                "<head>\n" +
                    head(model, request) +
                "</head>\n" +
                "<body>\n" +
                    "<div id=\"header\">\n" +
                        header(model, request) +
                    "</div>\n" +
                    "<hr/>\n" +

                    "<div id=\"content\">\n" +
                        body(model, request) +
                    "</div>\n" +

                    "<hr/>\n" +
                    "<div id=\"footer\">\n" +
                        footer(model, request) +
                    "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    protected final String head(Model model, HttpRequest request){

        //TODO global css import

        return  css(model, request) +
                js(model, request) +
                "<style>\n" +
                    style(model, request) +
                "</style>\n" +
                "<script type=\"application/javascript\">\n" +
                    script(model, request) +
                "</script>\n";
    }

    protected String header(Model model, HttpRequest request){
        StringBuilder html = new StringBuilder();
        html.append("<div id=\"auth\" align=\"right\">\n");

        if (! request.isSessionExpired()){
            //Show user info
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                html.append("<span>Hello, Guest</span> | <span><a href=\"/login\">login</a></span>");
            } else {
                html.append("<span>Hello, ").append(user.getName()).append("</span>\n");
                html.append(" | <span><a href=\"/logout\">logout</a></span>");
            }
        } else {
            //Show the "session expired" message
            html.append("<span>The session has expired, please <a href=\"/login\">login</a></span\n");
        }

        html.append("</div>\n");

        return html.toString();
    }

    protected String footer(Model model, HttpRequest request){
        return "<div align=\"center\">" +
                "<i>DAR project, STL - UPMC, 2016</i>" +
                "</div>";
    }

    protected String css(Model model, HttpRequest request){
        return "";
    }

    protected String js(Model model, HttpRequest request){
        return "";
    }

    protected String style(Model model, HttpRequest request){
        return "";
    }

    protected String script(Model model, HttpRequest request){
        return "";
    }

    protected abstract String body(Model model, HttpRequest request);
}
