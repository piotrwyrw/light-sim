package dev.vanadium.lightsim.ctl;

import dev.vanadium.lightsim.math.Vector;

public class ControlPointUtils {

    public static final int CONTROL_POINT_RADIUS = 40;

    public static boolean isInRadius(Vector a, Vector b) {
        return a.copy().sub(b).magnitude() <= CONTROL_POINT_RADIUS;
    }

}
