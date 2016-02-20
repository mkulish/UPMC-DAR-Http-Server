package edu.upmc.dar.applications.points.util;

import edu.upmc.dar.applications.points.entity.Point2D;

import java.util.List;

public class JsonUtil {

    public static String serializePoint(Point2D point) throws Exception {
        StringBuilder result = new StringBuilder();

        result.append("{");

        if(point.getId() != null) {
            result.append("\"id\":\"").append(point.getId()).append("\"");
        }
        result.append(",\"x\":\"").append(point.getX()).append("\"");
        result.append(",\"y\":\"").append(point.getY()).append("\"");

        result.append("}");
        return result.toString();
    }

    public static String serializePointsList(List<Point2D> points) throws Exception {
        StringBuilder result = new StringBuilder();

        result.append("[");

        for(Point2D point : points){
            result.append(serializePoint(point)).append(",");
        }

        if(! points.isEmpty()) {
            //remove the last comma
            result.deleteCharAt(result.length() - 1);
        }

        result.append("]");
        return result.toString();
    }
}
