package ui;

import model.Polynomial;
import model.Term;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/*
 * The class for starting the polynomial gui app.
 */
public class PolynomialGuiApp extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Polynomial polynomial;
    private static final String polynomialFilePath = "./data/polynomials.json";
    private GraphicsPanel graphicsPanel;
    private SummaryPanel summaryPanel;

    // EFFECTS: constructor; initializes and starts the polynomial gui app
    public PolynomialGuiApp() {
        super("Polynomial Root Calculator");
        init();
        run();
    }

    // MODIFIES: this
    // EFFECTS: initializes polynomial gui app
    private void init() {
        initPolynomial();
        initFrame();
        initUI();
    }


    // MODIFIES: this
    // EFFECTS: runs the polynomial gui app
    private void run() {
        ;
    }

    // MODIFIES: this, file storage
    // EFFECTS: initializes the polynomial and polynomial file (if not exists)
    private void initPolynomial() {
        polynomial = new Polynomial("0");
        File polynomialsFile = new File(polynomialFilePath);
        //if (!polynomialsFile.exists()) {
        //    generateSampleDataToFile();
        //}
    }

    // MODIFIES: this
    // EFFECTS: initializes the application's frame
    private void initFrame() {
        // main settings
        setTitle("CPSC 210: Alarm System Simulator");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // misc settings
        setBackground(Color.white);
        setUndecorated(false);

        // center on screen
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    // MODIFIES: this
    // EFFECTS: initializes the application's ui elements,
    // such as summary and graph
    private void initUI() {
        summaryPanel = new SummaryPanel(this.polynomial);
        add(summaryPanel, BorderLayout.EAST);

        graphicsPanel = new GraphicsPanel(this.polynomial);
        add(graphicsPanel, BorderLayout.WEST);

        addMenuBar();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds elements to the application's menu bar
    private void addMenuBar() {
        // set file menu
        JMenu fileMenu = new JMenu("Save/Load");
        fileMenu.setMnemonic('S');
        addMenuItem(fileMenu, new LoadAction(), KeyStroke.getKeyStroke("control L"));
        addMenuItem(fileMenu, new SaveAction(), KeyStroke.getKeyStroke("control S"));
        addMenuItem(fileMenu, new NewPolynomialAction(), KeyStroke.getKeyStroke("control N"));

        // set edit menu
        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, new NewTermAction(), KeyStroke.getKeyStroke("control A"));

        // add menus to menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }

    // EFFECTS: Adds item to menu (which may not be set to app yet)
    // Code copied from AlarmSystem project
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to load polynomial from file
    private class LoadAction extends AbstractAction {

        LoadAction() {
            super("Load from file");
        }

        @Override
        // EFFECTS: show list of polynomials from file, then allow user to select
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Create load dialog here!");
        }
    }

    // MODIFIES: this, file storage
    // EFFECTS: prepares action for when user wants to save polynomial to file
    private class SaveAction extends AbstractAction {

        SaveAction() {
            super("Save to file");
        }

        @Override
        // EFFECTS: save polynomial to file, then alert user
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Saved polynomial " + polynomial + "!");
        }
    }

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to load polynomial from input
    private class NewPolynomialAction extends AbstractAction {

        NewPolynomialAction() {
            super("New polynomial");
        }

        @Override
        // EFFECTS: create input dialog, then load polynomial from said input
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

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to add term from input
    private class NewTermAction extends AbstractAction {

        NewTermAction() {
            super("Add new term");
        }

        @Override
        // EFFECTS: create input dialog, then add term to loaded polynomial
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
