package dev.vanadium.lightsim.math;

import dev.vanadium.lightsim.ctl.ControlPointUtils;
import dev.vanadium.lightsim.visual.View;

import java.awt.*;

public class LinearSegment extends Mirror {

    private Vector origin;
    private Vector end;

    private boolean renderEndpoints = true;

    public LinearSegment(Vector origin, Vector end) {
        this.origin = origin;
        this.end = end;
        this.color = Color.decode("#27ae60");
    }

    public Vector direction() {
        return end.copy().sub(origin).normalize();
    }

    public Vector getOrigin() {
        return origin;
    }

    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    public Vector getEnd() {
        return end;
    }

    public void setEnd(Vector end) {
        this.end = end;
    }

    public LinearSegment noEndpoints() {
        this.renderEndpoints = false;
        return this;
    }

    @Override
    public Vector interpolate(double t) {
        return origin.copy().add(direction().copy().mul(t));
    }

    @Override
    public Intersection intersection(Ray ray) {
        Vector O = origin;
        Vector L = end.copy().sub(origin).normalize();
        Vector OR = ray.getOrigin();
        Vector D = ray.getDirection().copy().normalize();

        double Ox = O.getX();
        double Oy = O.getY();
        double Lx = L.getX();
        double Ly = L.getY();
        double ORx = OR.getX();
        double ORy = OR.getY();
        double Dx = D.getX();
        double Dy = D.getY();

        double sDenominator = Ly * Dx - Dy * Lx;
        double tDenominator = Ly * Dx - Dy * Lx;

        // The denominator cannot be 0, as division by 0 is illegal. If this would ever be the case,
        // if must mean there are no solutions for our intersection
        if (sDenominator == 0.0 || tDenominator == 0.0) {
            return null;
        }

        // Distance along the ray
        double t = -((ORx * Ly - ORy * Lx - Ox * Ly + Oy * Lx) / tDenominator);

        // Distance along the line segment
        double s = -((ORx * Dy - ORy * Dx - Ox * Dy + Oy * Dx) / sDenominator);

        // Up until this point, we've been mathematically treating the line segment as a ray.
        // To compensate for that, we need to check if the intersection is actually within the bounds
        // of the segment. If it isn't, there was technically no intersection, even if the equation
        // evaluates to a valid point.
        if (s <= 0 || s >= end.copy().sub(origin).magnitude()) {
            return null;
        }

        // We also only care about stuff that is in front of the ray
        if (t <= 0) {
            return null;
        }

        Vector point = interpolate(s);
        return new Intersection(ray, this, point);
    }

    @Override
    public Vector normal(Intersection point) {
        return new Vector(direction().getY(), -direction().getX()).normalize();
    }

    @Override
    public int mapControlPoint(Vector p) {
        if (ControlPointUtils.isInRadius(origin, p))
            return 0;

        if (ControlPointUtils.isInRadius(end, p))
            return 1;

        return -1;
    }

    @Override
    public void adjust(int controlPoint, Vector value) {
        if (controlPoint == 0)
            origin.set(value);

        if (controlPoint == 1)
            end.set(value);
    }

    @Override
    public void render(Graphics g) {
        if (renderEndpoints) {
            origin.render(g);
            end.render(g);
        }

        g.setColor(color);
        g.drawLine((int) origin.getX(), View.WINDOW_HEIGHT - (int) origin.getY(), (int) end.getX(), View.WINDOW_HEIGHT - (int) end.getY());
    }

    @Override
    public String toString() {
        return "LinearSegment{" +
                "origin=" + origin +
                ", end=" + end +
                '}';
    }

}
