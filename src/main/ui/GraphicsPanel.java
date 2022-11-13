package ui;

import model.Polynomial;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class GraphicsPanel extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private Polynomial polynomial;
    private static int MIDPOINT = 300;

    public GraphicsPanel(Polynomial polynomial) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.white);

        this.polynomial = polynomial;
    }

    public void update(Polynomial polynomial) {
        this.polynomial = polynomial;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        double unitPixelSize = 20;
        int unitsToRender = MIDPOINT / (int)unitPixelSize;

        drawAxis(g, unitPixelSize, unitsToRender);
        drawGraph(g, unitPixelSize, unitsToRender);
    }

    protected void drawAxis(Graphics g, double unitPixelSize, int unitsToRender) {
        // draw x/y axis
        g.drawLine(MIDPOINT,0, MIDPOINT, HEIGHT);
        g.drawLine(0, MIDPOINT, WIDTH, MIDPOINT);

        // draw unit indicators
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

    protected void drawGraph(Graphics g, double unitPixelSize, int unitsToRender) {
        Graphics2D g2 = (Graphics2D)g;

        Path2D positivePath = new Path2D.Double();
        Path2D negativePath = new Path2D.Double();
        double pointEval = polynomial.evaluateAtPoint(0);
        positivePath.moveTo(MIDPOINT, toChartCoordinatesY(pointEval, unitPixelSize));
        negativePath.moveTo(MIDPOINT, toChartCoordinatesY(pointEval, unitPixelSize));

        for (int i = 1; i < MIDPOINT; i++) {
            // get for positive values
            double point = (double)(i) / unitPixelSize;
            pointEval = polynomial.evaluateAtPoint(point);
            positivePath.lineTo(toChartCoordinatesX(point, unitPixelSize),
                    toChartCoordinatesY(pointEval, unitPixelSize));

            // get for negative values
            point *= -1;
            pointEval = polynomial.evaluateAtPoint(point);
            negativePath.lineTo(toChartCoordinatesX(point, unitPixelSize),
                    toChartCoordinatesY(pointEval, unitPixelSize));
        }
        g2.draw(positivePath);
        g2.draw(negativePath);
    }

    private double toChartCoordinatesX(double coordinateX, double unitPixelSize) {
        return (MIDPOINT + (unitPixelSize * coordinateX));
    }

    private double toChartCoordinatesY(double coordinateX, double unitPixelSize) {
        return (MIDPOINT - (unitPixelSize * coordinateX));
    }
}
