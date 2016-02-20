package edu.upmc.dar.applications.points.entity;

public class Point2D {
    private Integer id;
    private int x;
    private int y;

    public Point2D() {
    }

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point2D copy(){
        Point2D result = new Point2D(this.x, this.y);
        result.setId(this.id);
        return result;
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof Point2D && ((Point2D)other).x == this.x && ((Point2D)other).y == this.y);
    }

    @Override
    public int hashCode(){
        return (int)Math.round(Math.pow((x << 2) + 51, 2) + x * y + Math.pow((y << 3) - 247, 2));
    }
}
