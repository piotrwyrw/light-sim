package dev.vanadium.lightsim.visual;

import dev.vanadium.lightsim.math.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class View extends JPanel {

    public static int WINDOW_WIDTH = 1500;
    public static int WINDOW_HEIGHT = 800;

    private final JFrame frame;

    private LinearSegment seg;
    private Ray initialRay;

    private List<Mirror> mirrors;

    double angle = 0.0;

    public View() {
        super();

        frame = new JFrame("Light Simulator");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);

        mirrors = new ArrayList<>();
        mirrors.add(new LinearSegment(new Vector(600, 100), new Vector(700, 400)));
        mirrors.add(new LinearSegment(new Vector(300, 300), new Vector(400, 600)));

        initialRay = new Ray(new Vector(1200, 500), new Vector(-1, -0.1));

        new Timer(10, e -> {
            repaint();
        }).start();

    }

    private Intersection closestIntersectingMirror(Reflective lastMirror, Ray ray) {
        Intersection i = null;
        for (Reflective mirror : mirrors) {
            i = mirror.intersection(ray);
            if (i != null && lastMirror != mirror) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Render all mirrors
        mirrors.forEach(mirror -> {
            mirror.render(g);
        });

        Ray ray = initialRay;
        Intersection i = null;
        Reflective lastMirror = null;

        while (ray != null) {
            // Find a mirror that is in the path of the ray
            i = closestIntersectingMirror(lastMirror, ray);

            if (i == null)
                break;

            lastMirror = i.getObject();

            i.asSegment().colored(Color.decode("#dff9fb")).render(g);

            // Try to reflect the ray
            ray = i.getObject().reflection(ray);

            // Check if the ray intersects with any mirror, to determine whether to render
            // the ray in this pass, or let it be converted into a line segment during the next iteration
            if (closestIntersectingMirror(lastMirror, ray) == null)
                ray.render(g);
        }
    }

}
