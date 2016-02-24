package edu.upmc.dar.applications.points.servlet.data.points;


import edu.upmc.dar.applications.points.service.PointService;
import edu.upmc.dar.applications.points.service.impl.PointServiceImpl;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;
import edu.upmc.dar.server.util.JsonUtil;

@Servlet(url = "/p", method = RequestMethod.DELETE, produces = ContentType.APP_JSON)
public class PointsDeleteServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointService pointsService = PointServiceImpl.instance();

    @Override
    public void serve(HttpRequest request, HttpResponse response) throws Exception {
        String idParam = request.getParam("id");
        if(idParam != null){
            pointsService.delete(Integer.parseInt(idParam));
        }

        response.setBody(JsonUtil.serializeSubmitResponse(true, ""));
    }
}
