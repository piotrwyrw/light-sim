package dev.vanadium.lightsim.math;

import dev.vanadium.lightsim.ctl.ControlPointUtils;
import dev.vanadium.lightsim.visual.View;

import java.awt.*;
import java.util.List;
import java.util.OptionalDouble;


public class Circle extends Mirror {

    private Vector C;
    private Vector rVec;
    private double r;

    public Circle(Vector c, double r) {
        this.C = c;
        this.r = r;
        this.rVec = new Vector(C.getX() + r, C.getY());
        this.color = Color.decode("#27ae60");
    }

    public Vector getC() {
        return C;
    }

    public void setC(Vector c) {
        C = c;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    // This shape does not support interpolation
    @Override
    public Vector interpolate(double t) {
        return null;
    }

    // FIX IT
    @Override
    public Intersection intersection(Ray ray) {
        Vector O = ray.getOrigin();
        Vector d = ray.getDirection();
        Vector C = getC();
        double r2 = r * r;

        double a = d.dot(d);
        double b = -2 * (C.dot(d) - O.dot(d));
        double c = C.dot(C) - 2 * (C.dot(O)) + O.dot(O) - r2;

        List<Double> solutions = MathUtil.quadraticSolve(a, b, c).stream().filter(e -> e >= 0).toList();

        if (solutions.isEmpty())
            return null;

        OptionalDouble minSolution = solutions.stream().mapToDouble(Double::doubleValue).min();

        return new Intersection(ray, this, ray.interpolate(minSolution.getAsDouble()));
    }

    @Override
    public Vector normal(Intersection point) {
        Ray ray = point.getRay();

        // If the ray originated from within the circle
        if (ray.getOrigin().withinRadius(C, r)) {
            return this.C.copy().sub(point.getLocation()).normalize();
        }

        return point.getLocation().copy().sub(this.C).normalize();
    }

    @Override
    public Ray reflection(Ray incident) {
        Vector I = incident.getDirection().copy();
        Intersection intersection = intersection(incident);
        if (intersection == null)
            return null;
        Vector N = normal(intersection); // The normal is not point dependent in this case
        Vector R = I.copy().sub(N.copy().mul(2 * I.dot(N)));
        return R.asRay(intersection.getLocation());
    }

    @Override
    public int mapControlPoint(Vector p) {
        if (ControlPointUtils.isInRadius(C, p))
            return 0;

        if (ControlPointUtils.isInRadius(rVec, p))
            return 1;

        return -1;
    }

    @Override
    public void adjust(int controlPoint, Vector value) {
        if (controlPoint == 0) {
            Vector delta = this.C.copy().sub(value);
            this.C.set(value);
            this.rVec.map((vec) -> new Vector(vec.getX() - delta.getX(), vec.getY() - delta.getY()));
        } else if (controlPoint == 1) {
            this.rVec.set(value);
            this.r = this.C.copy().sub(this.rVec).magnitude();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.drawOval((int) C.getX() - (int) r, View.WINDOW_HEIGHT - (int) C.getY() - (int) r, (int) r * 2, (int) r * 2);

        C.render(g);
        rVec.render(g);
    }
}
