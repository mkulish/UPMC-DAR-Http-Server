package edu.upmc.dar.applications.points.servlet;


import edu.upmc.dar.applications.points.service.PointsService;
import edu.upmc.dar.applications.points.service.impl.PointsServiceImpl;
import edu.upmc.dar.applications.points.util.JsonUtil;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.request.HttpRequest;
import edu.upmc.dar.server.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/list", method = RequestMethod.GET)
public class PointsGetAllServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointsService pointsService = PointsServiceImpl.instance();

    @Override
    public void serve(HttpRequest request, HttpResponse response) throws Exception {
        response.setBody(JsonUtil.serializePointsList(pointsService.getAll()));
    }
}
