package dev.vanadium.lightsim.math;

public interface MirrorSurface {

    Intersection intersection(Ray ray);

    Ray reflection(Ray incident);

    Vector normal(Intersection point);

    int mapControlPoint(Vector p);

    void adjust(int controlPoint, Vector value);

}
