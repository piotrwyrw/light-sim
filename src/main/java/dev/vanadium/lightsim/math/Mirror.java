package dev.vanadium.lightsim.math;

import dev.vanadium.lightsim.visual.Renderable;

public abstract class Mirror extends Colorable implements Interpolable, Renderable, MirrorSurface {

    // The standard reflection method, that reflects the incoming ray about the normal
    @Override
    public Ray reflection(Ray incident) {
        Vector I = incident.getDirection().copy();
        Vector N = normal(null); // The normal is not point dependent in this case
        Vector R = I.copy().sub(N.copy().mul(2 * I.dot(N)));
        return R.asRay(intersection(incident).getLocation());
    }
}
