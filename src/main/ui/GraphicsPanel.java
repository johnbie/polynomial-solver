package ui;

import model.Polynomial;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/*
 * The class for showing the polynomial graph to user.
 */
public class GraphicsPanel extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private Polynomial polynomial;
    private static int MIDPOINT = 300;

    // EFFECTS: constructor; initializes and renders polynomial
    public GraphicsPanel(Polynomial polynomial) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.white);

        this.polynomial = polynomial;
    }

    // MODIFIES: this
    // EFFECTS: sets new polynomial and re-draws graph
    public void update(Polynomial polynomial) {
        this.polynomial = polynomial;
        repaint();
    }

    @Override
    // MODIFIES: this
    // EFFECTS: draws the polynomial graph
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        // init variables
        double unitPixelSize = 20;

        // draw
        drawAxes(g, unitPixelSize);
        drawGraph(g, unitPixelSize);
    }

    // MODIFIES: this
    // EFFECTS: draws the x and y axes, as well as unit indicators
    protected void drawAxes(Graphics g, double unitPixelSize) {
        // draw x/y axes
        g.drawLine(MIDPOINT,0, MIDPOINT, HEIGHT);
        g.drawLine(0, MIDPOINT, WIDTH, MIDPOINT);

        // draw unit indicators
        int unitsToRender = MIDPOINT / (int)unitPixelSize;
        int unitIndicatorLength = (int)(unitPixelSize / 10);
        for (int i = 1; i < unitsToRender; i++) {
            int unitPixelPos = MIDPOINT + (int)(unitPixelSize * i);
            g.drawLine(unitPixelPos, MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength);
            g.drawLine(MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength, unitPixelPos);

            // for negatives
            unitPixelPos = MIDPOINT - (int)(unitPixelSize * i);
            g.drawLine(unitPixelPos, MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength);
            g.drawLine(MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength, unitPixelPos);
        }
    }

    // MODIFIES: this
    // EFFECTS: draws the polynomial
    protected void drawGraph(Graphics g, double unitPixelSize) {
        Graphics2D g2 = (Graphics2D)g;

        // position positive and negative sides at y intercept
        Path2D positivePath = new Path2D.Double();
        Path2D negativePath = new Path2D.Double();
        double pointEval = polynomial.evaluateAtPoint(0);
        positivePath.moveTo(MIDPOINT, toChartCoordinatesY(pointEval, unitPixelSize));
        negativePath.moveTo(MIDPOINT, toChartCoordinatesY(pointEval, unitPixelSize));

        // draw a lotta lines
        for (int i = 1; i < MIDPOINT; i++) {
            // for positive side
            double point = (double)(i) / unitPixelSize;
            pointEval = polynomial.evaluateAtPoint(point);
            positivePath.lineTo(toChartCoordinatesX(point, unitPixelSize),
                    toChartCoordinatesY(pointEval, unitPixelSize));

            // for negative side
            point *= -1;
            pointEval = polynomial.evaluateAtPoint(point);
            negativePath.lineTo(toChartCoordinatesX(point, unitPixelSize),
                    toChartCoordinatesY(pointEval, unitPixelSize));
        }

        // draw positive and negative sides
        g2.draw(positivePath);
        g2.draw(negativePath);
    }

    // EFFECTS: returns the x UI position for the x axis position
    private double toChartCoordinatesX(double coordinateX, double unitPixelSize) {
        return (MIDPOINT + (unitPixelSize * coordinateX));
    }

    // EFFECTS: returns the y UI position for the y axis position
    private double toChartCoordinatesY(double coordinateX, double unitPixelSize) {
        return (MIDPOINT - (unitPixelSize * coordinateX));
    }
}
