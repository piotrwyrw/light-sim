package dev.vanadium.lightsim.math;

import java.awt.*;

public interface Reflective {

    Intersection intersection(Ray ray);

    Ray reflection(Ray incident);

    Vector normal(Intersection point);

}
