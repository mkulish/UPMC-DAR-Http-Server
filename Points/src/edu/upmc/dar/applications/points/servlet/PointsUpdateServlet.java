package edu.upmc.dar.applications.points.servlet;


import edu.upmc.dar.applications.points.entity.Point2D;
import edu.upmc.dar.applications.points.service.PointsService;
import edu.upmc.dar.applications.points.service.impl.PointsServiceImpl;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.request.HttpRequest;
import edu.upmc.dar.server.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;
import edu.upmc.dar.server.util.JsonUtil;

@Servlet(url = "/p", method = RequestMethod.PUT)
public class PointsUpdateServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointsService pointsService = PointsServiceImpl.instance();

    @Override
    public void serve(HttpRequest request, HttpResponse response) throws Exception {
        String idParam = request.getParam("id");
        String xParam = request.getParam("x");
        String yParam = request.getParam("y");

        if(idParam != null){
            Point2D point = new Point2D();
            point.setId(Integer.parseInt(idParam));

            if(xParam != null){
                point.setX(Integer.parseInt(xParam));
            }
            if(yParam != null){
                point.setY(Integer.parseInt(yParam));
            }

            pointsService.saveOrUpdate(point);
        }

        response.setBody(JsonUtil.serializeSubmitResponse(true, ""));
    }
}
