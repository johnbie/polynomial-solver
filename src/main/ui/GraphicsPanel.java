package ui;

import model.Polynomial;
import model.Root;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

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
        int unitsToRender = getUnitsToRender();
        double unitPixelSize = (double)MIDPOINT / unitsToRender;

        // draw
        drawAxes(g, unitsToRender, unitPixelSize);
        drawGraph(g, unitPixelSize);
    }

    // MODIFIES: this
    // EFFECTS: draws the x and y axes, as well as unit indicators
    protected void drawAxes(Graphics g, int unitsToRender, double unitPixelSize) {
        // draw x/y axes
        g.drawLine(MIDPOINT,0, MIDPOINT, HEIGHT);
        g.drawLine(0, MIDPOINT, WIDTH, MIDPOINT);

        // draw unit indicators
        int unitIndicatorLength = 1;
        int magnitude = 1;

        while (unitsToRender > 30) {
            unitsToRender /= 10;
            unitsToRender += 1;
            unitPixelSize *= 10;
            unitIndicatorLength *= 2;
            magnitude *= 10;
        }

        for (int i = 1; i < unitsToRender; i++) {
            String displayString = (i * magnitude) + "";

            int unitPixelPos = MIDPOINT + (int)(unitPixelSize * i);
            g.drawLine(unitPixelPos, MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength);
            g.drawLine(MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength, unitPixelPos);
            g.drawString(displayString, unitPixelPos - (displayString.length() * 3), MIDPOINT + 17);
            g.drawString("-" + displayString, MIDPOINT - ((1 + displayString.length()) * 7), unitPixelPos + 4);

            // for negatives
            unitPixelPos = MIDPOINT - (int)(unitPixelSize * i);
            g.drawLine(unitPixelPos, MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength);
            g.drawLine(MIDPOINT - unitIndicatorLength, unitPixelPos, MIDPOINT + unitIndicatorLength, unitPixelPos);
            g.drawString("-" + displayString, unitPixelPos - ((1 + displayString.length()) * 3), MIDPOINT + 17);
            g.drawString(displayString, MIDPOINT - (1 + displayString.length() * 7) - 2, unitPixelPos + 4);
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

    // EFFECTS: returns the new max value to resize graph to
    private int getUnitsToRender() {
        // start with y intercept
        double maxValue = Math.max(10, Math.abs(polynomial.evaluateAtPoint(0)));

        maxValue = getNewMaxVal(maxValue, polynomial.getXIntercepts());
        maxValue = getNewMaxVal(maxValue, polynomial.getCriticalPoints());
        maxValue = getNewMaxVal(maxValue, polynomial.getInflectionPoints());

        return (int)(maxValue * 1.25) + 1;
    }

    // EFFECTS: returns the largest value from previous max value, roots, and value of roots
    private double getNewMaxVal(double max, List<Root> rootList) {
        for (Root root : rootList) {
            double rootVal = Math.abs(root.getValue());
            max = Math.max(max, rootVal);

            double solution = Math.abs(polynomial.evaluateAtPoint(rootVal));
            max = Math.max(max, solution);
        }
        return max;
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
