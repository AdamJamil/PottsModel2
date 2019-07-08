package com.company;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static com.company.Main.n;

class GUtil
{
    private static int xShift = 0, yShift = 0;
    private static int xOff = 40, yOff = 90;
    private static double dist = 50;
    static double width = 800, height = yOff + dist * (n + 2);
    private static int caseIndex = 0, upsetIndex = 0, partialOrderIndex = 0;
    private Driver d;

    GUtil(Driver d, GraphicsContext gc)
    {
        this.d = d;
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(0.1), event ->
        {
            gc.clearRect(0, 0, width, height);

            gc.beginPath();
            gc.moveTo(40, 40);
            gc.arcTo(40, 40, 100, 100, 20);
            gc.closePath();
            gc.setFill(Color.BLACK);
            gc.setStroke(Color.BLACK);
            gc.fill();
            gc.stroke();

            drawDiagram(gc);
            drawPartialOrdering(gc);
        }));

        timeline.play();
    }

    private void drawPartialOrdering(GraphicsContext gc)
    {
        boolean[][] partialOrder = d.partialOrder, minRel = d.minRel();
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < partialOrder.length; i++)
        {
            boolean[] set = minRel[i];
            set[i] = false;

            for (int j = 0; j < set.length; j++)
                if (set[j])
                {
                    boolean oneStep = false;
                    for (int k = 0; k < Driver.neighbors[i].length; k++)
                        if (Driver.neighbors[i][k] == j)
                            oneStep = true;

                    if (oneStep)
                    {
                        gc.setStroke(Color.BLACK);
                        drawArrow(gc, xPos(Driver.states.get(j)), yPos(Driver.states.get(j)),
                                xPos(Driver.states.get(i)), yPos(Driver.states.get(i)));
                    }
                    else
                    {
                        gc.setStroke(Color.BLUE);
                        drawArrow(gc, xPos(Driver.states.get(j)), yPos(Driver.states.get(j)),
                                13 + (xPos(Driver.states.get(i)) + xPos(Driver.states.get(j))) / 2,
                                -13 + (yPos(Driver.states.get(i)) + yPos(Driver.states.get(j))) / 2);

                        drawArrow(gc, 13 + (xPos(Driver.states.get(i)) + xPos(Driver.states.get(j))) / 2,
                                -13 + (yPos(Driver.states.get(i)) + yPos(Driver.states.get(j))) / 2,
                                xPos(Driver.states.get(i)), yPos(Driver.states.get(i)));
                    }
                }
        }
    }

    private double yPos(State s)
    {
        return yOff + dist * (n - s.order[0]) + yShift;
    }

    private double xPos(State s)
    {
        return xOff + dist * s.order[2] + xShift;
    }

    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2)
    {
        x1 += 5;
        x2 += 5;
        y1 += 5;
        y2 += 5;

        gc.strokeLine(x1, y1, x2, y2);

        double cx = (x1 + x2) / 2, cy = (y1 + y2) / 2;

        double Dx = x2 - x1, Dy = y2 - y1;
        double dx = 4 * Dx / Math.sqrt(Dx * Dx + Dy * Dy);
        double dy = 4 * Dy / Math.sqrt(Dx * Dx + Dy * Dy);

        double cos = 0.866, sin = 0.5;

        gc.strokeLine(cx + dx * cos - dy * sin, cy + dx * sin + dy * cos, cx, cy);
        gc.strokeLine(cx + dx * cos + dy * sin, cy - dx * sin + dy * cos, cx, cy);
    }

    private void drawDiagram(GraphicsContext gc)
    {
        gc.setStroke(Color.BLACK);

        for (int i = 1; i <= n + 1; i++)
        {
            int rowStates = (i + 1) / 2;

            for (int j = 0; j < rowStates; j++)
                gc.strokeOval(xOff + dist * j + xShift, yOff + dist * (i - 1) + yShift, 10, 10);
        }
    }

    private double convertX(double in)
    {
        return 37.5 * in + 300;
    }

    private double convertY(double in)
    {
        //10px is 1
        return -50 * in + 300;
    }
}