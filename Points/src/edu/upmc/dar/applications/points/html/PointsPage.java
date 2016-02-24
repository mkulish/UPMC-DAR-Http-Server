package edu.upmc.dar.applications.points.html;

import edu.upmc.dar.applications.points.entity.Point2D;
import edu.upmc.dar.server.html.HtmlProducer;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.model.Model;

import java.util.List;
import java.util.Map;

public class PointsPage extends HtmlProducer {
    private static PointsPage instance;
    private PointsPage(){}

    public static PointsPage instance(){
        if(instance == null){
            instance = new PointsPage();
        }
        return instance;
    }

    @Override
    protected String style(Model model, HttpRequest request){
        return  "#pointsTable{\n" +
                    "font-size: 16;\n" +
                "}\n" +
                "#pointsTable td {\n" +
                    "min-width: 100px;\n" +
                "}\n" +
                "#pointsTable tr.header {\n" +
                    "height: 40px;\n" +
                    "font-weight: bold;\n" +
                    "font-size: 20;\n" +
                "}\n";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String body(Model model, HttpRequest request){
        StringBuilder html = new StringBuilder();
        html.append("<span><h3>Currently available points:</h3></span>\n");

        List<Point2D> points = (List<Point2D>)model.get("points");
        if(points != null && ! points.isEmpty()){
            html.append("<table id=\"pointsTable\">\n");
            html.append("<tr class=\"header\"><td>ID</td><td>X</td><td>Y</td></tr>\n");

            for(Point2D point : points){
                html.append("<tr><td>").append(point.getId())
                        .append("</td><td>").append(point.getX())
                        .append("</td><td>").append(point.getY())
                        .append("</td></tr>\n");
            }
            html.append("</table>\n");
        } else {
            html.append("<span><i>No points found</i></span>\n");
        }

        return html.toString();
    }
}
