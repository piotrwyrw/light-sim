package dev.vanadium.lightsim.math;

import dev.vanadium.lightsim.visual.Renderable;
import dev.vanadium.lightsim.visual.View;

import java.awt.*;
import java.util.function.Function;

public class Vector extends Colorable implements Renderable {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector from(Point point) {
        return new Vector(point.x, point.y);
    }

    public static Vector zero() {
        return new Vector(0.0, 0.0);
    }

    public void set(Vector another) {
        this.x = another.x;
        this.y = another.y;
    }

    public double dot(Vector another) {
        return x * another.x + y * another.y;
    }

    public double magnitude() {
        return Math.sqrt(dot(this));
    }

    public Ray asRay(Vector origin) {
        return new Ray(origin, this);
    }

    public Vector mul(double d) {
        this.x *= d;
        this.y *= d;
        return this;
    }

    public Vector truncated() {
        this.x = Math.ceil(x);
        this.y = Math.ceil(this.y);
        return this;
    }

    public Vector map(Function<Vector, Vector> mapping) {
        set(mapping.apply(this));
        return this;
    }

    public Vector div(double d) {
        this.x /= d;
        this.y /= d;
        return this;
    }

    public Vector normalize() {
        double len = magnitude();
        if (len == 0.0)
            return this;
        div(len);
        return this;
    }

    public Vector add(Vector another) {
        this.x += another.x;
        this.y += another.y;
        return this;
    }

    public Vector sub(Vector another) {
        this.x -= another.x;
        this.y -= another.y;
        return this;
    }

    public boolean is(Vector another) {
        return this.x == another.x && this.y == another.y;
    }

    public boolean withinRadius(Vector of, double radius) {
        return copy().sub(of).magnitude() <= radius;
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector compensateInversion() {
        this.y = View.WINDOW_HEIGHT - this.y;
        return this;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval((int) x - 4, View.WINDOW_HEIGHT - (int) y - 4, 8, 8);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
