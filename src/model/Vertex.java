package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class Vertex implements Vectorizable<Vertex> {
    Point3D point;
    Col color;
    // + Normala
    // + UV

    public Vertex(int x, int y, Double z, Col color) {
        point = new Point3D(x, y, z);
        this.color = color;
    }
    public Vertex(Point3D point, Col color) {
        this.point = point;
        this.color = color;
    }
    public Vertex(Point3D point) {
        this.point = point;
        this.color = new Col(0x005555);
    }
    public Vertex(Vertex v) {
        this.point = v.point;
        this.color = v.color;

    }

    public Point3D getPoint() {

        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
    }

    public void setPoint(Vec3D point){
        this.point = new Point3D(point);
    }

    public Col getColor() {
        return color;
    }

    public void setColor(Col color) {}

    @Override
    public Vertex mul(double k) {
        return new Vertex(point.mul(k), color.mul(k));
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(point.add(v.getPoint()), color.add(v.getColor()));
    }
}
