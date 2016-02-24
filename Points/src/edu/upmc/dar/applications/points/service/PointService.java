package edu.upmc.dar.applications.points.service;


import edu.upmc.dar.applications.points.entity.Point2D;

import java.util.List;

public interface PointService {
    List<Point2D> getAll();
    Point2D get(Integer id);
    Point2D saveOrUpdate(Point2D point);
    void delete(Point2D point);
    void delete(Integer id);
}
