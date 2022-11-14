package ui;

import model.Polynomial;

import javax.swing.*;

/*
 * The class for showing the polynomial summary to user.
 */
public class SummaryPanel extends JPanel {
    private JTextArea summary;

    // EFFECTS: constructor; initializes and shows polynomial summary
    public SummaryPanel(Polynomial polynomial) {
        // set summary
        summary = new JTextArea(30, 15);
        summary.setText(getSummary(polynomial));
        summary.setEditable(false);
        add(summary);
    }

    // MODIFIES: this
    // EFFECTS: updates the polynomial summary
    public void update(Polynomial polynomial) {
        summary.setText(getSummary(polynomial));
        repaint();
    }

    // EFFECTS: generates the polynomial summary
    private String getSummary(Polynomial polynomial) {
        String summary = "\nSummary for " + polynomial + ": " + "\n";
        summary += " - x-intercepts: " + polynomial.getXIntercepts() + "\n";
        summary += " - y-intercept: " + polynomial.getYIntercept() + "\n";
        summary += " - critical points: " + polynomial.getCriticalPoints() + "\n";
        summary += " - inflection points: " + polynomial.getInflectionPoints() + "\n";
        summary += " - derivative: " + polynomial.getDerivative();
        return summary;
    }
}