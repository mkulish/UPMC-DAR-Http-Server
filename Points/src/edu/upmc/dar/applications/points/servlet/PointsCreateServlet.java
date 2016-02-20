package edu.upmc.dar.applications.points.servlet;


import edu.upmc.dar.applications.points.entity.Point2D;
import edu.upmc.dar.applications.points.service.PointsService;
import edu.upmc.dar.applications.points.service.impl.PointsServiceImpl;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.request.HttpRequest;
import edu.upmc.dar.server.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/p", method = RequestMethod.POST, produces = ContentType.APP_JSON)
public class PointsCreateServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointsService pointsService = PointsServiceImpl.instance();

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
