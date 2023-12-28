package dev.vanadium.lightsim.ctl;

import dev.vanadium.lightsim.math.MirrorSurface;

public class ControlPoint {

    private int id;
    private MirrorSurface holder;

    public ControlPoint(int id, MirrorSurface holder) {
        this.id = id;
        this.holder = holder;
    }

    public MirrorSurface getHolder() {
        return holder;
    }

    public void setHolder(MirrorSurface holder) {
        this.holder = holder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
