package ui;

import model.Polynomial;

import javax.swing.*;

public class SummaryPanel extends JPanel {
    private Polynomial polynomial;
    private JTextArea summary;

    public SummaryPanel(Polynomial polynomial) {
        this.polynomial = polynomial;

        summary = new JTextArea(30, 15);
        summary.setText(getSummary());
        JScrollPane scrollPane = new JScrollPane(summary);
        summary.setEditable(false);
        add(summary);
    }

    public void update(Polynomial polynomial) {
        this.polynomial = polynomial;
        summary.setText(getSummary());
        repaint();
    }

    private String getSummary() {
        String summary = "\nSummary for " + polynomial + ": " + "\n";
        summary += " - x-intercepts: " + polynomial.getXIntercepts() + "\n";
        summary += " - y-intercept: " + polynomial.getYIntercept() + "\n";
        summary += " - critical points: " + polynomial.getCriticalPoints() + "\n";
        summary += " - inflection points: " + polynomial.getInflectionPoints() + "\n";
        summary += " - derivative: " + polynomial.getDerivative();
        return summary;
    }
}
