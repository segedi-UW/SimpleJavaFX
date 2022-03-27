package com.sdc.javafx_demo;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Random;

public class CanvasController {


    @FXML private Canvas canvas;
    private AnimationTimer animationTimer;
    private double mx, my;
    private String hint;
    private Random random;

    private LinkedList<Explosion> explosions;


    @FXML
    private void initialize() {
        System.out.println("Canvas is null in initialize(): " + (canvas == null));
        canvas.setFocusTraversable(true);

        // mouse handling (updateMouse is below)
        canvas.setOnMouseMoved(this::updateMouse);
        canvas.setOnMouseClicked(this::updateMouse);

        // cannot use a lambda with an abstract class
        animationTimer = new AnimationTimer() {
            // handle is called to match 60 fps
            @Override
            public void handle(long now) {
                GraphicsContext c = canvas.getGraphicsContext2D();
                // clear the background
                c.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
                // draw a circle at the cursor
                if (canvas.contains(mx,my)) {
                    c.fillOval(mx, my, 4, 4);
                    c.fillText(hint, mx, my+30);
                }

                // common graphics methods
                explosions.forEach(e -> {
                    e.tick();
                    e.draw(c);
                });
                // remove dead explosions
                explosions.removeIf(e -> e.ticks <= 0);
            }
        };
        animationTimer.start();
        System.out.println("Started");
    }

    private void updateMouse(MouseEvent mouseEvent) {
        mx = mouseEvent.getX();
        my = mouseEvent.getY();
        if (mouseEvent.getClickCount() == 1 && canvas.contains(mx, my)) {
            hint = "Pretty cool eh?!";
            for (int i = 0; i < 100; i++) {
                double ry = (random.nextDouble())*2.0 * (random.nextFloat() > 0.5 ? 1 : -1);
                double rx = (random.nextDouble())*2.0 * (random.nextFloat() > 0.5 ? 1 : -1);
                double rr = random.nextFloat();
                double rg = random.nextFloat();
                double rb = random.nextFloat();
                explosions.add(new Explosion(mx, my, rx, ry, new Color(rr, rg, rb, 1.0)));
            }
        }
    }

    public CanvasController() {
        // note that the canvas is always null here!
        System.out.println("Canvas is null in constructor: " + (canvas == null));
        hint = "Try clicking!";
        random = new Random();
        explosions = new LinkedList<>();
    }

    private static class Explosion {
        private double x, y;
        private double vx, vy;
        private Color color;
        private final int oTicks = 60*3;
        private int ticks;

        public Explosion(double x, double y, double vx, double vy, Color color) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.ticks = oTicks;
        }

        public void tick() {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (float)ticks/(float)oTicks);
            this.x += vx;
            this.y += vy;
            ticks--;
        }

        public void draw(GraphicsContext c) {
            // save the current context
            c.save();
            // can make changes freely
            c.setFill(color);
            c.fillRect(x, y, 3, 3);

            // restore the old context
            c.restore();
        }

    }
}
