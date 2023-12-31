package dev.vanadium.lightsim.math;

import dev.vanadium.lightsim.visual.Renderable;
import dev.vanadium.lightsim.visual.View;

import java.awt.*;

public class Ray extends Colorable implements Interpolable, Renderable {

    private Vector origin;
    private Vector direction;

    private boolean renderOrigin = true;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.copy().normalize();
    }

    public Vector getOrigin() {
        return origin;
    }

    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction.copy().normalize();
    }

    public Ray noOriginRendering() {
        this.renderOrigin = false;
        return this;
    }

    @Override
    public void render(Graphics g) {
        Vector end = interpolate(View.WINDOW_HEIGHT + View.WINDOW_WIDTH);

        g.setColor(color);
        g.drawLine((int) origin.getX(), View.WINDOW_HEIGHT - (int) origin.getY(), (int) end.getX(), View.WINDOW_HEIGHT - (int) end.getY());

        if (renderOrigin) {
            g.setColor(Color.decode("#1abc9c"));
            g.fillOval((int) origin.getX() - 4, View.WINDOW_HEIGHT - (int) origin.getY() - 4, 8, 8);
        }
    }

    @Override
    public Vector interpolate(double t) {
        return origin.copy().add(direction.copy().mul(t));
    }

    @Override
    public String toString() {
        return "Ray{" +
                "origin=" + origin +
                ", direction=" + direction +
                '}';
    }
}
