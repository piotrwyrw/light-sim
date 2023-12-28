package dev.vanadium.lightsim.visual;

import dev.vanadium.lightsim.ctl.ControlPoint;
import dev.vanadium.lightsim.math.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class View extends JPanel {

    public static final int WINDOW_WIDTH = 1500;
    public static final int WINDOW_HEIGHT = 800;

    public static final int BOUNCE_LIMIT = 10000;

    public static final double EPSILON = 0.1;

    private final JFrame frame;

    private List<Ray> rays;

    private List<Mirror> mirrors;

    private ControlPoint controlPoint = null;

    private Vector mouse;

    private List<Vector> tmpSegPoints;

    private boolean pathInspection = false;

    public View() {
        super();

        frame = new JFrame("Light Simulator");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);

        tmpSegPoints = new ArrayList<>();

        mirrors = new ArrayList<>();
        mirrors.add(new LinearSegment(new Vector(500, 100), new Vector(700, 400)));
        mirrors.add(new LinearSegment(new Vector(300, 300), new Vector(400, 600)));

        Vector rayDirection = new Vector(-1, 0);

        rays = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            rays.add(new Ray(new Vector(1200, 500 - i * 3), rayDirection));
        }

        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    selectControlPoint(e.getPoint());
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    tmpSegPoints.add(Vector.from(e.getPoint()).compensateInversion());
                    if (tmpSegPoints.size() == 2) {
                        mirrors.add(new LinearSegment(tmpSegPoints.get(0), tmpSegPoints.get(1)));
                        tmpSegPoints.clear();
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON2) {
                    pathInspection = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    controlPoint = null;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouse = Vector.from(e.getPoint());
                mouse.setY(View.WINDOW_HEIGHT - mouse.getY());

                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (controlPoint == null)
                        return;

                    controlPoint.getHolder().adjust(controlPoint.getId(), Vector.from(e.getPoint()).map(vec -> new Vector(vec.getX(), WINDOW_HEIGHT - vec.getY())));
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouse = Vector.from(e.getPoint());
                mouse.setY(View.WINDOW_HEIGHT - mouse.getY());
            }
        });

        new Timer(10, e -> {
            repaint();
        }).start();

    }

    private Intersection closestIntersectingMirror(Intersection lastIntersection, Ray ray) {
        Intersection i = null;
        double distance = Integer.MAX_VALUE;
        for (MirrorSurface mirror : mirrors) {
            Intersection tmpInter = mirror.intersection(ray);

            if (tmpInter == null)
                continue;

            double tmpDistance = tmpInter.getLocation().copy().sub(ray.getOrigin()).magnitude();

            if (distance > tmpDistance && (lastIntersection == null || (lastIntersection.getLocation().copy().sub(tmpInter.getLocation()).magnitude() > EPSILON))) {
                i = tmpInter;
                distance = tmpDistance;
            }
        }

        return i;
    }

    private void selectControlPoint(Point mouse) {
        Vector mouseVec = Vector.from(mouse).map(vec -> new Vector(vec.getX(), WINDOW_HEIGHT - vec.getY()));
        for (MirrorSurface mirror : mirrors) {
            int ctl = mirror.mapControlPoint(mouseVec);
            if (ctl < 0)
                continue;
            controlPoint = new ControlPoint(ctl, mirror);
            return;
        }

        controlPoint = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        tmpSegPoints.forEach(point -> point.render(g));

        // Render all mirrors
        mirrors.forEach(mirror -> {
            mirror.render(g);
        });

        if (pathInspection) {
            System.out.println("##########\nPATH INSPECTION\n##########\n");
        }

        for (Ray ray : rays) {
            Intersection i = null;
            Intersection lastIntersection = null;
            int bounces = 0;

            while (ray != null && bounces < BOUNCE_LIMIT) {
                // Find a mirror that is in the path of the ray
                i = closestIntersectingMirror(lastIntersection, ray);

                if (i == null) {
                    break;
                }

                i.getObject().normal(i).render(g);

                lastIntersection = i;

                i.asSegment().noEndpoints().colored(Color.decode("#dff9fb")).render(g);

                // Try to reflect the ray
                ray = i.getObject().reflection(ray);

                if (pathInspection) {
                    System.out.println();
                    System.out.println("-----");
                    System.out.println(ray);
                    System.out.println("Intersects with");
                    System.out.println(i.getObject());
                    System.out.println("At");
                    System.out.println(i.getLocation());
                    System.out.println("& Last intersection at");
                    System.out.println(lastIntersection.getLocation().copy().truncated());
                    System.out.println("Reflected ray");
                    System.out.println(ray);
                    System.out.println("-----");
                    System.out.println();
                }

                // Check if the ray intersects with any mirror, to determine whether to render
                // the ray in this pass, or let it be converted into a line segment during the next iteration
                if (closestIntersectingMirror(lastIntersection, ray) == null)
                    ray.noOriginRendering().colored(Color.decode("#2c3e50")).render(g);

                bounces++;
            }

            if (mouse != null)
                mouse.render(g);
        }

        pathInspection = false;
    }

}
