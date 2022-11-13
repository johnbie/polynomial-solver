package ui;

import model.Polynomial;
import model.Term;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/*
 * The class for starting the polynomial gui app.
 */
public class PolynomialGuiApp extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Polynomial polynomial;
    private static final String polynomialFilePath = "./data/polynomials.json";
    private List<Shape> shapes;
    private GraphicsPanel graphicsPanel;
    private SummaryPanel summaryPanel;

    public PolynomialGuiApp() {
        super("Polynomial Root Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);

        setTitle("CPSC 210: Alarm System Simulator");
        setSize(WIDTH, HEIGHT);

        shapes = new ArrayList<>();
        setBackground(Color.white);

        init();
        run();
    }

    public void init() {
        polynomial = new Polynomial("x^2");

        centreOnScreen();

        summaryPanel = new SummaryPanel(this.polynomial);
        add(summaryPanel, BorderLayout.EAST);

        graphicsPanel = new GraphicsPanel(this.polynomial);
        add(graphicsPanel, BorderLayout.WEST);
        drawPolynomial();

        addMenu();
        setVisible(true);
    }

    private void drawPolynomial() {
        graphicsPanel.update(polynomial);
    }

    // Centres frame on desktop
    // modifies: this
    // effects:  location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    public void run() {
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        // set file menu
        JMenu fileMenu = new JMenu("Save/Load");
        fileMenu.setMnemonic('S');
        addMenuItem(fileMenu, new LoadAction(), KeyStroke.getKeyStroke("control L"));
        addMenuItem(fileMenu, new SaveAction(), KeyStroke.getKeyStroke("control S"));
        addMenuItem(fileMenu, new NewPolynomialAction(), KeyStroke.getKeyStroke("control N"));

        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, new NewTermAction(), KeyStroke.getKeyStroke("control A"));

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Adds an item with given handler to the given menu
     * Code copied from AlarmSystem project
     * @param theMenu  menu to which new item is added
     * @param action   handler for new menu item
     * @param accelerator    keystroke accelerator for this menu item
     */
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    private class LoadAction extends AbstractAction {

        LoadAction() {
            super("Load from file");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Create load dialog here!");
        }
    }

    private class SaveAction extends AbstractAction {

        SaveAction() {
            super("Save to file");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Saved polynomial " + polynomial + "!");
        }
    }

    private class NewPolynomialAction extends AbstractAction {

        NewPolynomialAction() {
            super("New polynomial");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String polyStr = JOptionPane.showInputDialog(null,
                    "Polynomial",
                    "New Polynomial",
                    JOptionPane.QUESTION_MESSAGE);

            try {
                if (polyStr != null) {
                    polynomial = new Polynomial(polyStr);
                    summaryPanel.update(polynomial);
                    graphicsPanel.update(polynomial);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class NewTermAction extends AbstractAction {

        NewTermAction() {
            super("Add new term");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String termStr = JOptionPane.showInputDialog(null,
                    "Term",
                    "Add new term",
                    JOptionPane.QUESTION_MESSAGE);

            try {
                if (termStr != null) {
                    Term term = new Term(termStr);
                    polynomial.addTerm(term);
                    summaryPanel.update(polynomial);
                    graphicsPanel.update(polynomial);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
