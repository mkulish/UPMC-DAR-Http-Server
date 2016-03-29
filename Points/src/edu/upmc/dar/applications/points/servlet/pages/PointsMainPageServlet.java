package edu.upmc.dar.applications.points.servlet.pages;


import edu.upmc.dar.applications.points.service.PointService;
import edu.upmc.dar.applications.points.service.impl.PointServiceImpl;
import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;

@Servlet(url = "/points", method = RequestMethod.GET)
public class PointsMainPageServlet extends HttpServlet {
    //No dependencies injection for the moment
    private PointService pointsService = PointServiceImpl.instance();

    @Override
    public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
        model.put("points", pointsService.getAll());

        return "points/main/main-page";
    }
}
