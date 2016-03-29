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
import edu.upmc.dar.server.util.JsonUtil;

@Servlet(url = "/p/{id}", method = RequestMethod.PUT, produces = ContentType.APP_JSON)
public class PointsUpdateServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointService pointsService = PointServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        String idParam = request.getUrlParam("id");
        String xParam = request.getParam("x");
        String yParam = request.getParam("y");

        if(idParam != null){
            Point2D point = pointsService.get(Integer.parseInt(idParam));
            if(point != null){
                if (xParam != null) {
                    point.setX(Integer.parseInt(xParam));
                }
                if (yParam != null) {
                    point.setY(Integer.parseInt(yParam));
                }
                pointsService.saveOrUpdate(point);
                return JsonUtil.serializeSubmitResponse(true, "");
            } else {
                return JsonUtil.serializeSubmitResponse(false, "Point with id " + idParam + " not found");
            }
        } else {
            return JsonUtil.serializeSubmitResponse(false, "Id param not found");
        }
    }
}
