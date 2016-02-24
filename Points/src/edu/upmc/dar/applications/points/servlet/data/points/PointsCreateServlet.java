package edu.upmc.dar.applications.points.servlet.data.points;


import edu.upmc.dar.applications.points.entity.Point2D;
import edu.upmc.dar.applications.points.service.PointService;
import edu.upmc.dar.applications.points.service.impl.PointServiceImpl;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/p", method = RequestMethod.POST, produces = ContentType.APP_JSON)
public class PointsCreateServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointService pointsService = PointServiceImpl.instance();

    @Override
    public void serve(HttpRequest request, HttpResponse response) throws Exception {
        String xParam = request.getParam("x");
        String yParam = request.getParam("y");

        if(xParam != null && yParam != null){
            Point2D point = new Point2D(Integer.parseInt(xParam), Integer.parseInt(yParam));
            pointsService.saveOrUpdate(point);
            response.setBody("{\"id\":"+ point.getId() +"}");
        }
    }
}
