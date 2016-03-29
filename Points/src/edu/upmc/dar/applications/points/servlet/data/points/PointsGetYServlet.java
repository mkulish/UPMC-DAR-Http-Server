package edu.upmc.dar.applications.points.servlet.data.points;


import edu.upmc.dar.applications.points.entity.Point2D;
import edu.upmc.dar.applications.points.service.PointService;
import edu.upmc.dar.applications.points.service.impl.PointServiceImpl;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/p/{id}/y", method = RequestMethod.GET, produces = ContentType.APP_JSON)
public class PointsGetYServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointService pointsService = PointServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        Point2D point = null;

        String idParam = request.getUrlParam("id");
        if(idParam != null){
            point = pointsService.get(Integer.parseInt(idParam));
        }

        if(point != null){
            return "" + point.getY();
        } else {
            return "";
        }
    }
}
