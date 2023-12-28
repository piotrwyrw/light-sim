package dev.vanadium.lightsim.math;

import dev.vanadium.lightsim.visual.Renderable;

import java.awt.*;

public abstract class Colorable implements Renderable {

    protected Color color = Color.WHITE;

    public Colorable colored(Color color) {
        this.color = color;
        return this;
    }

}
