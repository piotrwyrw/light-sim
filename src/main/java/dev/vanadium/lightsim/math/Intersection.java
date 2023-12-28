package dev.vanadium.lightsim.math;

import dev.vanadium.lightsim.visual.Renderable;
import dev.vanadium.lightsim.visual.View;

import java.awt.*;

public class Intersection extends Colorable implements Renderable {

    private Ray ray;
    private MirrorSurface object;

    private Vector location;

    public Intersection(Ray ray, MirrorSurface object, Vector location) {
        this.ray = ray;
        this.object = object;
        this.location = location;
    }

    public LinearSegment asSegment() {
        return new LinearSegment(ray.getOrigin(), location);
    }

    public Ray getRay() {
        return ray;
    }

    public void setRay(Ray ray) {
        this.ray = ray;
    }

    public MirrorSurface getObject() {
        return object;
    }

    public void setObject(MirrorSurface object) {
        this.object = object;
    }

    public Vector getLocation() {
        return location;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval((int) location.getX() - 6, View.WINDOW_HEIGHT - (int) location.getY() - 6, 12, 12);
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "ray=" + ray +
                ", object=" + object +
                ", location=" + location +
                '}';
    }
}
