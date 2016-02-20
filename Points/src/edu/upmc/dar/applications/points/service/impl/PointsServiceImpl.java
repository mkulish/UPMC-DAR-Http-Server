package edu.upmc.dar.applications.points.service.impl;

import edu.upmc.dar.applications.points.entity.Point2D;
import edu.upmc.dar.applications.points.service.PointsService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PointsServiceImpl implements PointsService {
    private static PointsServiceImpl instance;
    private PointsServiceImpl(){}

    public static synchronized PointsServiceImpl instance(){
        if(instance == null){
            instance = new PointsServiceImpl();
        }
        return instance;
    }

    private List<Point2D> list = new LinkedList<>();
    private int nextIdentity = 0;

    @Override
    public synchronized List<Point2D> getAll() {
        List<Point2D> listCopy = new ArrayList<>(list.size());
        list.forEach(point -> listCopy.add(point.copy()));
        return listCopy;
    }

    @Override
    public synchronized Point2D get(Integer id) {
        if(id == null) return null;
        return list.stream().filter(point -> id.equals(point.getId())).findFirst().orElse(null);
    }

    @Override
    public synchronized Point2D saveOrUpdate(Point2D point) {
        if(point != null){
            Point2D existing = get(point.getId());
            if(existing != null){
                //Updating the point
                existing.setX(point.getX());
                existing.setY(point.getY());
                point.setId(existing.getId());
                return existing;
            } else {
                //Saving the new point
                Point2D newPoint = point.copy();
                newPoint.setId(nextIdentity++);
                point.setId(newPoint.getId());
                list.add(newPoint);
                return newPoint;
            }
        }

        return null;
    }

    @Override
    public synchronized void delete(Point2D point) {
        if(point != null){
            delete(point.getId());
        }
    }

    @Override
    public synchronized void delete(Integer id) {
        Point2D existing = get(id);
        if(existing != null){
            list.remove(existing);
        }
    }
}
